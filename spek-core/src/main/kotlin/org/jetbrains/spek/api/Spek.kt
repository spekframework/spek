package org.jetbrains.spek.api

import org.jetbrains.spek.junit.JUnitClassRunner
import org.junit.runner.RunWith
import java.util.*

@RunWith(JUnitClassRunner::class)
abstract class Spek : org.jetbrains.spek.api.Specification {

    private val recordedGivenActions = LinkedList<TestGivenAction>()
    private val recordedBeforeEachActions = LinkedList<()->Unit>()
    private val recordedAfterEachActions = LinkedList<()->Unit>()

    override fun beforeEach(action: () -> Unit) {
        recordedBeforeEachActions.add(action)
    }

    override fun afterEach(action: () -> Unit) {
        recordedAfterEachActions.add(action)
    }

    override fun given(description: String, givenExpression: org.jetbrains.spek.api.Given.() -> Unit) {
        recordedGivenActions.add(
                object : org.jetbrains.spek.api.TestGivenAction {
                    val given: GivenImpl by lazy {
                        val impl = GivenImpl()
                        impl.givenExpression() // Delay given expresion execution, let it be executed by test runner
                        impl
                    }

                    override fun description() = "given " + description

                    override fun listOn() = given.listOn()

                    override fun run(action: () -> Unit) {
                        recordedBeforeEachActions.forEach { it() }
                        action()
                        recordedAfterEachActions.forEach { it() }
                    }
                })

    }

    fun listGiven() = recordedGivenActions

    fun allGiven(): List<org.jetbrains.spek.api.TestGivenAction> = recordedGivenActions
}


fun <T> Spek.givenData(data: Iterable<T>, givenExpression: org.jetbrains.spek.api.Given.(T) -> Unit) {
    for (entry in data) {
        given(entry.toString()) {
            givenExpression(entry)
        }
    }
}
