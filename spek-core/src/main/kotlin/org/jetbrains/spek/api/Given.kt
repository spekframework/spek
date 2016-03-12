package org.jetbrains.spek.api

import java.util.*
import kotlin.collections.forEach

open class GivenImpl : org.jetbrains.spek.api.Given {
    private val recordedActions = LinkedList<TestOnAction>()
    private val beforeActions = LinkedList<() -> Unit>()
    private val afterActions = LinkedList<() -> Unit>()

    fun listOn() = recordedActions

    override fun beforeOn(it: () -> Unit) {
        beforeActions.add(it)
    }

    override fun afterOn(it: () -> Unit) {
        afterActions.add(it)
    }

    override fun on(description: String, onExpression: org.jetbrains.spek.api.On.() -> Unit) {
        recordedActions.add(
                object : org.jetbrains.spek.api.TestOnAction {
                    val on: OnImpl by lazy {
                        val impl = OnImpl()
                        impl.onExpression() // Delay on expression execution, let it be executed by test runner
                        impl
                    }

                    override fun description() = "on " + description

                    override fun listIt() = on.listIt()

                    override fun run(action: () -> Unit) {
                        beforeActions.forEach { it() }
                        action()
                        afterActions.forEach { it() }
                    }
                })
    }
}

