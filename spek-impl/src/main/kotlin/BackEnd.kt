package org.spek.impl

import org.spek.api.*

public trait TestFixtureAction {
    fun description(): String
    fun iterateGiven(it:(TestGivenAction) -> Unit)
}

public trait TestGivenAction {
    fun description(): String
    fun iterateOn(it: (TestOnAction) -> Unit)
}

public trait TestOnAction {
    fun description(): String
    fun iterateIt(it : (TestItAction) -> Unit)
}

public trait TestItAction {
    fun description(): String
    fun run()
}

open public class SpekImpl: Specification {
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

    public fun iterateGiven(it:(TestGivenAction) -> Unit) : Unit = removingIterator(recordedActions, it)

    public fun allGiven(): List<TestGivenAction> = recordedActions
}

public class GivenImpl: Given {
    private val recordedActions = linkedListOf<TestOnAction>()
    private val beforeActions = linkedListOf<()->Unit>()
    private val afterActions = linkedListOf<()->Unit>()

    public fun iterateOn(callback : (TestOnAction) -> Unit) : Unit = removingIterator(recordedActions) {
        beforeActions forEach { it() }
        try {
            callback(it)
        } finally {
            afterActions forEach { it() }
        }
    }

    override fun beforeOn(it: () -> Unit) {
        beforeActions add it
    }

    override fun afterOn(it: () -> Unit) {
        afterActions add it
    }

    public override fun on(description: String, onExpression: On.() -> Unit) {
        recordedActions.add(
                object : TestOnAction {
                    public override fun description() = "on " + description
                    public override fun iterateIt(it: (TestItAction) -> Unit) {
                        val on = OnImpl()
                        on.onExpression()
                        return on.iterateIt(it)
                    }
                })
    }
}

public class OnImpl: On {
    private val recordedActions = linkedListOf<TestItAction>()

    public fun iterateIt(it : (TestItAction) -> Unit) : Unit = removingIterator(recordedActions, it)

    public override fun it(description: String, itExpression: It.()->Unit) {
        recordedActions.add(
                object : TestItAction {
                    public override fun description() = "it " + description
                    public override fun run() = ItImpl().itExpression()
                })
    }
}

open class ItImpl : It {}

