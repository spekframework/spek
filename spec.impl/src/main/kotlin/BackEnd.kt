package org.spek.impl

import org.spek.api.*

public trait TestFixtureAction {
    fun description(): String
    fun performInit(): List<TestGivenAction>
}

public trait TestGivenAction {
    fun description(): String
    fun performInit(): List<TestOnAction>
}

public trait TestOnAction {
    fun description(): String
    fun performInit(): List<TestItAction>
}

public trait TestItAction {
    fun description(): String
    fun run()
}

public class SpekImpl: Spek {
    private val recordedActions = arrayListOf<TestGivenAction>()

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        recordedActions.add(
                object : TestGivenAction {
                    public override fun description() = "given " + description
                    public override fun performInit(): List<TestOnAction> {
                        val given = GivenImpl()
                        given.givenExpression()
                        return given.getActions()
                    }
                })
    }

    fun allGivens(): List<TestGivenAction> = recordedActions
}

public class GivenImpl: Given {
    private val recordedActions = arrayListOf<TestOnAction>()

    public fun getActions(): List<TestOnAction> = recordedActions

    public override fun on(description: String, onExpression: On.() -> Unit) {
        recordedActions.add(
                object : TestOnAction {
                    public override fun description() = description
                    public override fun performInit(): List<TestItAction> {
                        val on = OnImpl()
                        on.onExpression()
                        return on.getActions()
                    }
                })
    }
}

public class OnImpl: On {
    private val recordedActions = arrayListOf<TestItAction>()

    public fun getActions(): List<TestItAction> = recordedActions

    public override fun it(description: String, itExpression: It.()->Unit) {
        recordedActions.add(
                object : TestItAction {
                    public override fun description() = description
                    public override fun run() = It().itExpression()
                })
    }
}

