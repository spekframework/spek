package org.jetbrains.spek.api

import java.util.*

open class OnImpl : On {
    private val recordedActions = LinkedList<TestItAction>()

    fun iterateIt(it: (TestItAction) -> Unit) {
        removingIterator(recordedActions, it)
    }

    override fun it(description: String, itExpression: It.() -> Unit) {
        recordedActions.add(
                object : TestItAction {
                    override fun description() = "it " + description
                    override fun run() {
                        ItImpl().itExpression()
                    }
                })
    }
}

