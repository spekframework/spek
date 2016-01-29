package org.jetbrains.spek.api

import kotlin.collections.linkedListOf

open class OnImpl: On {
    private val recordedActions = linkedListOf<TestItAction>()

    fun iterateIt(it : (TestItAction) -> Unit) {
        removingIterator(recordedActions, it)
    }

    override fun it(description: String, itExpression: It.()->Unit) {
        recordedActions.add(
                object : TestItAction {
                    override fun description() = "it " + description
                    override fun run() {
                        ItImpl().itExpression()
                    }
                })
    }
}

