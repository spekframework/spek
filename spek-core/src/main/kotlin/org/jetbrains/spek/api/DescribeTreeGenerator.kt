package org.jetbrains.spek.api

import java.util.*

class DescribeTreeGenerator : DescribeBody {
    private val recordedActions = LinkedList<TestAction>()
    private val recordedAfterEaches = LinkedList<() -> Unit>()

    fun recordedActions(): List<TestAction> {
        if (recordedActions.isEmpty()) {
            throw RuntimeException(this.javaClass.canonicalName + ": no tests found")
        }
        return recordedActions
    }

    override fun describe(description: String, evaluateBody: DescribeBody.() -> Unit) {
        val inner = DescribeTreeGenerator()
        recordedActions.add(
                object : TestAction {
                    override fun recordedActions(): List<TestAction> {
                        return inner.recordedActions
                    }

                    override fun description() = description
                    override fun type() = ActionType.DESCRIBE
                    override fun run(notifier: Notifier) {
                        notifier.start(this)

                        for ((index, it) in inner.recordedActions().withIndex()) {
                            val executor = DescribeExecutor(index)
                            executor.evaluateBody()
                            executor.run(notifier)
                        }

                        notifier.succeed(this)
                    }
                }
        )

        inner.evaluateBody()
    }

    override fun xdescribe(description: String, evaluateBody: DescribeBody.() -> Unit) {
        val inner = DescribeTreeGenerator()
        recordedActions.add(
                object : TestAction {
                    override fun recordedActions(): List<TestAction> {
                        return inner.recordedActions
                    }

                    override fun description() = description
                    override fun type() = ActionType.DESCRIBE
                    override fun run(notifier: Notifier) {
                        notifier.ignore(this)
                    }
                }
        )

        inner.evaluateBody()
    }

    override fun xit(description: String, @Suppress("UNUSED_PARAMETER") assertions: () -> Unit) {
        recordedActions.add(
                object : TestAction {
                    override fun recordedActions(): List<TestAction> {
                        throw UnsupportedOperationException()
                    }

                    override fun description() = "it " + description

                    override fun run(notifier: Notifier) {
                        notifier.ignore(this)
                    }

                    override fun type() = ActionType.IT
                }
        )
    }

    override fun it(description: String, assertions: () -> Unit) {
        recordedActions.add(
                object : TestAction {
                    override fun recordedActions(): List<TestAction> {
                        throw UnsupportedOperationException()
                    }

                    override fun description() = "it " + description
                    override fun type() = ActionType.IT

                    override fun run(notifier: Notifier) {
                        notifier.start(this)
                        try {
                            assertions()
                            notifier.succeed(this)
                        } catch(e: Throwable) {
                            notifier.fail(this, e)
                        }

                        // TODO: if an afterEach throws, it should mark this test as failed
                        recordedAfterEaches.forEach {
                            it()
                        }

                    }
                }
        )
    }

    override fun afterEach(actions: () -> Unit) {
        recordedAfterEaches.add(actions)
    }
}