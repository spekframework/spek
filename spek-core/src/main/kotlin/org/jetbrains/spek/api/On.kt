package org.jetbrains.spek.api

open class OnImpl : On {
    private val recordedItActions = linkedListOf<TestItAction>()

    fun listIt() = recordedItActions

    override fun it(description: String, itExpression: It.() -> Unit) {
        recordedItActions.add(
                object : TestItAction {
                    override fun description() = "it " + description

                    override fun run(action: () -> Unit) {
                        ItImpl().itExpression()
                    }
                })
    }
}

