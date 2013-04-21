package org.spek.impl

import org.spek.api.*
import java.util.ArrayList

public trait SkipAction {
    fun why(): String
}

trait ExecutionState {
    fun makeTestGivenAction(description: String, givenExpression: Given.() -> Unit): TestGivenAction
    fun makeTestOnAction(description: String, onExpression: On.() -> Unit): TestOnAction
    fun makeTestItAction(description: String, itExpression: It.()->Unit): TestItAction
}

class NormalState: ExecutionState {

    override fun makeTestItAction(description: String, itExpression: It.()->Unit): TestItAction {
        return object : TestItAction {

            public override fun description() = description

            public override fun run() = It().itExpression()
        }
    }

    override fun makeTestOnAction(description: String, onExpression: On.() -> Unit): TestOnAction {
        return object : TestOnAction {

            public override fun description() = description

            public override fun performInit(): List<TestItAction> {
                val o = OnImpl()
                o.onExpression()
                return o.getActions()
            }
        }
    }

    override fun makeTestGivenAction(description: String, givenExpression: Given.() -> Unit): TestGivenAction {
        return object:TestGivenAction {
            public override fun description() = "given " + description
            public override fun performInit(): List<TestOnAction> {
                val g = GivenImpl()
                g.givenExpression()
                return g.getActions()
            }
        }
    }
}

class SkipState: ExecutionState {

    var reason: String = ""

    override fun makeTestItAction(description: String, itExpression: It.()->Unit): TestItAction {

        val reasonSnapshot = reason

        return object : SkipAction, TestItAction {

            public override fun description(): String = description

            public override fun run() {}

            public override fun why() = reasonSnapshot
        }
    }

    override fun makeTestOnAction(description: String, onExpression: On.() -> Unit): TestOnAction {

        val reasonSnapshot = reason

        return object : SkipAction, TestOnAction {

            public override fun description() = description

            public override fun performInit() = ArrayList<TestItAction>()

            public override fun why() = reasonSnapshot
        }
    }

    override fun makeTestGivenAction(description: String, givenExpression: Given.() -> Unit): TestGivenAction {

        val reasonSnapshot = reason

        return object: SkipAction, TestGivenAction {

            public override fun description() = description

            public override fun performInit() = ArrayList<TestOnAction>()

            public override fun why() = reasonSnapshot
        }
    }
}

class StateActionManager {

    private val normalState = NormalState()
    private val skipState = SkipState()

    private var state: ExecutionState = normalState

    public fun state(): ExecutionState = state

    public fun normal() {
        state = normalState
    }

    public fun skip(why: String) {
        skipState.reason = why
        state = skipState
    }
}
