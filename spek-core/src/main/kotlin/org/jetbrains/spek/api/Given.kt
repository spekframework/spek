package org.jetbrains.spek.api


open class GivenImpl : org.jetbrains.spek.api.Given {
    private val recordedOnActions = linkedListOf<org.jetbrains.spek.api.TestOnAction>()
    private val recordedBeforeActions = linkedListOf<() -> Unit>()
    private val recordedAfterActions = linkedListOf<() -> Unit>()

    public fun listOn() = recordedOnActions

    public override fun beforeOn(it: () -> Unit) {
        recordedBeforeActions.add(it)
    }

    public override fun afterOn(it: () -> Unit) {
        recordedAfterActions.add(it)
    }

    public override fun on(description: String, onExpression: org.jetbrains.spek.api.On.() -> Unit) {
        recordedOnActions.add(
                object : org.jetbrains.spek.api.TestOnAction {
                    val on: OnImpl by lazy {
                        val impl = OnImpl()
                        impl.onExpression() // Delay on expression execution, let it be executed by test runner
                        impl
                    }

                    public override fun description() = "on " + description

                    override fun listIt() = on.listIt()

                    override fun run(action: () -> Unit) {
                        recordedBeforeActions.forEach { it() }
                        action()
                        recordedAfterActions.forEach { it() }
                    }
                })
    }
}

