package org.spek.impl

import org.spek.*

open class GivenImpl: Given {
    private val recordedActions = linkedListOf<TestOnAction>()
    private val beforeActions = linkedListOf<()->Unit>()
    private val afterActions = linkedListOf<()->Unit>()

    public fun iterateOn(callback : (TestOnAction) -> Unit) {
        removingIterator(recordedActions) {
            // This doesn't actually work. Tests pass but tests are wrong
            beforeActions.forEach { it() }
            try {
                callback(it)
            } finally {
                afterActions.forEach { it() }
            }
        }
    }

    override fun beforeOn(codeBlock: () -> Unit) {
        beforeActions.add(codeBlock)
    }

    override fun afterOn(codeBlock: () -> Unit) {
        afterActions.add(codeBlock)
    }

    public override fun on(description: String, onExpression: On.() -> Unit) {
        recordedActions.add(
                object : TestOnAction {
                    public override fun description() = "on " + description
                    public override fun iterateIt(it: (TestItAction) -> Unit) {
                        val on = OnImpl()
                        on.onExpression()
                        return on.iterateIt(it)
                    }
                })
    }
}

