package org.jetbrains.spek.api

import java.util.*
import kotlin.collections.linkedListOf

open class OnImpl: On {
    private val recordedActions = LinkedList<TestItAction>()

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

