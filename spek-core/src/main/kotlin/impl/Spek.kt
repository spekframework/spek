package org.spek

import org.junit.runner.RunWith
import org.spek.junit.*
import org.spek.impl.*

RunWith(javaClass<JUnitClassRunner<*>>())
public abstract class Spek : Specification {

    private val recordedActions = linkedListOf<TestGivenAction>()

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        recordedActions.add(
                object : TestGivenAction {
                    public override fun description() = "given " + description

                    public override fun iterateOn(it: (TestOnAction) -> Unit) {
                        val given = GivenImpl()
                        given.givenExpression()
                        given.iterateOn(it)
                    }
                })

    }

    public fun iterateGiven(it: (TestGivenAction) -> Unit): Unit = removingIterator(recordedActions, it)

    public fun allGiven(): List<TestGivenAction> = recordedActions
}


public fun <T> Spek.givenData(data: Iterable<T>, givenExpression: Given.(T) -> Unit) {
    for (entry in data) {
        given("given ${entry.toString()}") {
            givenExpression(entry)
        }
    }
}
