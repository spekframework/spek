package org.jetbrains.spek.api

import java.util.*

open class OnImpl : On {
    private val recordedActions = LinkedList<TestItAction>()

    fun listIt() = recordedActions

    override fun it(description: String, itExpression: It.() -> Unit) {
        recordedActions.add(
                object : TestItAction {
                    override fun description() = "it " + description
                    override fun run(action: () -> Unit) {
                        ItImpl().itExpression()
                    }
                })
    }
}

