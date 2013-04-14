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

public fun givenImpl(description: String, givenExpression: Given.() -> Unit): TestGivenAction {
    return object:TestGivenAction {
        public override fun description(): String = "given " + description
        public override fun performInit(): List<TestOnAction> {
            val g = GivenImpl()
            g.givenExpression()
            return g.getActions()
        }
    }
}

public class GivenImpl: Given {
    private val recordedActions = arrayListOf<TestOnAction>()

    public fun getActions(): List<TestOnAction> = recordedActions

    public override fun on(description: String, onExpression: On.() -> Unit) {
        recordedActions.add(
                object : TestOnAction {
                    public override fun description(): String = description
                    public override fun performInit(): List<TestItAction> {
                        val o = OnImpl()
                        o.onExpression()
                        return o.getActions()
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
                    public override fun description(): String = description
                    public override fun run() {
                        It().itExpression()
                    }
                }
        )
    }
}
