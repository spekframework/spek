package org.spek.impl

import org.spek.*

open class GivenImpl: Given {
    private val recordedActions = linkedListOf<TestOnAction>()
    private val beforeActions = linkedListOf<()->Unit>()
    private val afterActions = linkedListOf<()->Unit>()

    public fun iterateOn(callback : (TestOnAction) -> Unit) : Unit = removingIterator(recordedActions) {
        beforeActions forEach { it() }
        try {
            callback(it)
        } finally {
            afterActions forEach { it() }
        }
    }

    override fun beforeOn(it: () -> Unit) {
        beforeActions add it
    }

    override fun afterOn(it: () -> Unit) {
        afterActions add it
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

