package org.jetbrains.spek.api

import java.util.*


open class GivenImpl : org.jetbrains.spek.api.Given {
    private val recordedActions = LinkedList<TestOnAction>()
    private val beforeActions = LinkedList<() -> Unit>()
    private val afterActions = LinkedList<() -> Unit>()

    fun iterateOn(callback: (org.jetbrains.spek.api.TestOnAction) -> Unit) {
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


    override fun on(description: String, onExpression: org.jetbrains.spek.api.On.() -> Unit) {
        recordedActions.add(
                object : org.jetbrains.spek.api.TestOnAction {
                    override fun description() = "on " + description
                    override fun iterateIt(it: (org.jetbrains.spek.api.TestItAction) -> Unit) {
                        val on = org.jetbrains.spek.api.OnImpl()
                        on.onExpression()
                        return on.iterateIt(it)
                    }
                })
    }
}

