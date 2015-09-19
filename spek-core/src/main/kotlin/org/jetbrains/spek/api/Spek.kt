package org.jetbrains.spek.api

import org.junit.runner.RunWith
import org.jetbrains.spek.junit.*
import org.jetbrains.spek.api.*

@RunWith(JUnitClassRunner::class)
public abstract class Spek : org.jetbrains.spek.api.Specification {

    private val recordedActions = linkedListOf<org.jetbrains.spek.api.TestGivenAction>()

    public override fun given(description: String, givenExpression: org.jetbrains.spek.api.Given.() -> Unit) {
        recordedActions.add(
                object : org.jetbrains.spek.api.TestGivenAction {
                    public override fun description() = "given " + description

                    public override fun iterateOn(it: (org.jetbrains.spek.api.TestOnAction) -> Unit) {
                        val given = org.jetbrains.spek.api.GivenImpl()
                        given.givenExpression()
                        given.iterateOn(it)
                    }
                })

    }

    public fun iterateGiven(it: (org.jetbrains.spek.api.TestGivenAction) -> Unit): Unit = org.jetbrains.spek.api.removingIterator(recordedActions, it)

    public fun allGiven(): List<org.jetbrains.spek.api.TestGivenAction> = recordedActions
}


public fun <T> Spek.givenData(data: Iterable<T>, givenExpression: org.jetbrains.spek.api.Given.(T) -> Unit) {
    for (entry in data) {
        given(entry.toString()) {
            givenExpression(entry)
        }
    }
}
