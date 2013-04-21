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
    private val stateManager = StateActionManager()

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        recordedActions.add(stateManager.state().makeTestGivenAction(description, givenExpression))
        stateManager.normal()
    }

    override fun skip(why: String): Spek {
        stateManager.skip(why)
        return this
    }

    fun allGivens(): List<TestGivenAction> = recordedActions
}

public class GivenImpl: Given {
    private val recordedActions = arrayListOf<TestOnAction>()
    private val stateManager = StateActionManager()

    public fun getActions(): List<TestOnAction> = recordedActions

    public override fun on(description: String, onExpression: On.() -> Unit) {
        recordedActions.add(stateManager.state().makeTestOnAction(description, onExpression))
        stateManager.normal()
    }

    override fun skip(why: String): Given {
        stateManager.skip(why)
        return this
    }
}

public class OnImpl: On {
    private val recordedActions = arrayListOf<TestItAction>()
    private val stateManager = StateActionManager()

    public fun getActions(): List<TestItAction> = recordedActions

    public override fun it(description: String, itExpression: It.()->Unit) {
        recordedActions.add(stateManager.state().makeTestItAction(description, itExpression))
        stateManager.normal()
    }

    override fun skip(why: String): On {
        stateManager.skip(why)
        return this
    }
}

