package org.jetbrains.spek.api

import java.util.*

class DescribeParser() : DescribeBody {
    private val children = LinkedList<TestAction>()
    val befores: LinkedList<() -> Unit> = LinkedList()
    val afters: LinkedList<() -> Unit> = LinkedList()

    fun children(): List<TestAction> {
        if (children.isEmpty()) {
            throw RuntimeException(this.javaClass.canonicalName + ": no tests found")
        }
        return children
    }

    override fun describe(description: String, evaluateBody: DescribeBody.() -> Unit) {
        val inner = DescribeParser()
        children.add(
                object : TestAction {
                    override fun children(): List<TestAction> {
                        return inner.children
                    }

                    override fun description() = description
                    override fun type() = ActionType.DESCRIBE
                    override fun run(notifier: Notifier, parentContext: SpekContext) {
                        notifier.start(this)

                        inner.children().forEach {
                            it.run(notifier, object : SpekContext {
                                override fun run(test: () -> Unit) {
                                    parentContext.run {
                                        befores.forEach { it() }
                                        test()
                                        afters.forEach { it() }
                                    }
                                }
                            })
                        }

                        notifier.succeed(this)
                    }
                }
        )

        inner.evaluateBody()
    }

    override fun xdescribe(description: String, evaluateBody: DescribeBody.() -> Unit) {
        val inner = DescribeParser()
        children.add(
                object : TestAction {
                    override fun children(): List<TestAction> {
                        return inner.children
                    }

                    override fun description() = description
                    override fun type() = ActionType.DESCRIBE
                    override fun run(notifier: Notifier, parentContext: SpekContext) {
                        notifier.ignore(this)
                    }
                }
        )

        inner.evaluateBody()
    }

    override fun xit(description: String, @Suppress("UNUSED_PARAMETER") assertions: () -> Unit) {
        children.add(
                object : TestAction {
                    override fun children(): List<TestAction> {
                        throw UnsupportedOperationException()
                    }

                    override fun description() = "it " + description

                    override fun run(notifier: Notifier, parentContext: SpekContext) {
                        notifier.ignore(this)
                    }

                    override fun type() = ActionType.IT
                }
        )
    }

    override fun it(description: String, assertions: () -> Unit) {
        children.add(
                object : TestAction {
                    override fun children(): List<TestAction> {
                        throw UnsupportedOperationException()
                    }

                    override fun description() = "it " + description
                    override fun type() = ActionType.IT

                    override fun run(notifier: Notifier, parentContext: SpekContext) {
                        notifier.start(this)
                        try {
                            parentContext.run {
                                befores.forEach { it() }
                                assertions()
                                afters.forEach { it() }
                            }
                            notifier.succeed(this)
                        } catch(e: Throwable) {
                            notifier.fail(this, e)
                        }

                    }
                }
        )
    }

    override fun beforeEach(actions: () -> Unit) {
        befores.add(actions)
    }

    override fun afterEach(actions: () -> Unit) {
        afters.add(actions)
    }
}