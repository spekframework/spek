package org.jetbrains.spek.api

import org.jetbrains.spek.api.*


open class GivenImpl: org.jetbrains.spek.api.Given {
    private val recordedActions = linkedListOf<org.jetbrains.spek.api.TestOnAction>()
    private val beforeActions = linkedListOf<()->Unit>()
    private val afterActions = linkedListOf<()->Unit>()

    public fun iterateOn(callback : (org.jetbrains.spek.api.TestOnAction) -> Unit) {
        org.jetbrains.spek.api.removingIterator(recordedActions) {
            // This doesn't actually work. Tests pass but tests are wrong
            beforeActions.forEach { it() }
            try {
                callback(it)
            } finally {
                afterActions.forEach { it() }
            }
        }
    }

    public override fun beforeOn(codeBlock: () -> Unit) {
        beforeActions.add(codeBlock)
    }

    public override fun afterOn(codeBlock: () -> Unit) {
        afterActions.add(codeBlock)
    }

    public override fun on(description: String, onExpression: org.jetbrains.spek.api.On.() -> Unit) {
        recordedActions.add(
                object : org.jetbrains.spek.api.TestOnAction {
                    public override fun description() = "on " + description
                    public override fun iterateIt(it: (org.jetbrains.spek.api.TestItAction) -> Unit) {
                        val on = org.jetbrains.spek.api.OnImpl()
                        on.onExpression()
                        return on.iterateIt(it)
                    }
                })
    }
}

