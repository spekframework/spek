package org.jetbrains.spek.api

import org.jetbrains.spek.junit.JUnitClassRunner
import org.junit.runner.RunWith

@RunWith(JUnitClassRunner::class)
public abstract class Spek : org.jetbrains.spek.api.Specification {

    private val recordedGivenActions = linkedListOf<org.jetbrains.spek.api.TestGivenAction>()

    public override fun given(description: String, givenExpression: org.jetbrains.spek.api.Given.() -> Unit) {
        recordedGivenActions.add(
                object : org.jetbrains.spek.api.TestGivenAction {
                    val given: GivenImpl by lazy {
                        val impl = GivenImpl()
                        impl.givenExpression() // Delay given expresion execution, let it be executed by test runner
                        impl
                    }

                    public override fun description() = "given " + description

                    override fun listOn() = given.listOn()

                    override fun run(action: () -> Unit) {
                        action()
                    }
                })

    }

    public fun listGiven() = recordedGivenActions

    public fun allGiven(): List<org.jetbrains.spek.api.TestGivenAction> = recordedGivenActions
}


public fun <T> Spek.givenData(data: Iterable<T>, givenExpression: org.jetbrains.spek.api.Given.(T) -> Unit) {
    for (entry in data) {
        given(entry.toString()) {
            givenExpression(entry)
        }
    }
}
