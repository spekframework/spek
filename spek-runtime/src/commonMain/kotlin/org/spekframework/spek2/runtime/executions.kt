package org.spekframework.spek2.runtime

import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeAction
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

interface ExecutionContext

class ExecutionUnit(
    private val beforeGroup: MutableList<ScopeAction>,
    private val afterGroup: MutableList<ScopeAction>,
    private val beforeEachTest: MutableList<ScopeAction>,
    private val afterEachTest: MutableList<ScopeAction>,
    private val tests: MutableList<TestBody.() -> Unit>
) {
    fun execute(context: ExecutionContext) {
        try {
            // before group fixtures and declare scope values (group & scope caching modes) in order
            beforeGroup.forEach {
                when (it) {
                    is ScopeAction.Fixture -> it.cb()
                    is ScopeAction.Value<*> -> {
                        // re-init scope value
                        when (it.cachingMode) {
                            CachingMode.GROUP, CachingMode.EACH_GROUP -> {
                                // do something
                            }
                            CachingMode.SCOPE -> {
                                // do something
                            }
                            else -> throw AssertionError("Invalid caching mode.")
                        }
                    }
                    else -> throw AssertionError("Invalid action in before group phase")
                }
            }

            tests.forEach { test ->

                try {
                    // before each test fixtures and declare scope values (test caching mode) in order
                    beforeEachTest.forEach {
                        when (it) {
                            is ScopeAction.Fixture -> it.cb()
                            is ScopeAction.Value<*> -> {
                                // re-init scope value
                                when (it.cachingMode) {
                                    CachingMode.TEST -> {
                                        // do something
                                    }
                                    else -> throw AssertionError("Invalid caching mode.")
                                }
                            }
                        }
                    }

                    // execute test(s)
                    test(object : TestBody {
                        override fun <T> memoized(): MemoizedValue<T> {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                } finally {
                    // after each test fixtures and destruct scope values (test caching mode) in order
                    afterEachTest.forEach {
                        when (it) {
                            is ScopeAction.Fixture -> it.cb()
                            is ScopeAction.Value<*> -> {
                                // re-init scope value
                                when (it.cachingMode) {
                                    CachingMode.TEST -> {
                                        // call destructor
                                    }
                                    else -> throw AssertionError("Invalid caching mode.")
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            // after group fixtures and destruct scope values (group & scope caching modes) in order
            afterGroup.forEach {
                when (it) {
                    is ScopeAction.Fixture -> it.cb()
                    is ScopeAction.Value<*> -> {
                        // re-init scope value
                        when (it.cachingMode) {
                            CachingMode.GROUP, CachingMode.EACH_GROUP -> {
                                // call destructor
                            }
                            CachingMode.SCOPE -> {
                                // call destructor
                            }
                            else -> throw AssertionError("Invalid caching mode.")
                        }
                    }
                }
            }
        }
    }
}

typealias Fixture = () -> Unit
class ScopeValue<T>(val cachingMode: CachingMode, val factory: () -> T, val destructor: (T) -> Unit)

class ExecutionUnitTemplate {
    private val actions = mutableListOf<ScopeAction>()
    private val tests = mutableListOf<TestBody.() -> Unit>()

    fun addAction(action: ScopeAction): ExecutionUnitTemplate {
        this.actions.add(action)
        return this
    }

    fun addActions(actions: List<ScopeAction>): ExecutionUnitTemplate {
        this.actions.addAll(actions)
        return this
    }

    fun build(): ExecutionUnit {
        val beforeGroup = mutableListOf<ScopeAction>()
        val afterGroup = mutableListOf<ScopeAction>()
        val beforeEachTest = mutableListOf<ScopeAction>()
        val afterEachTest = mutableListOf<ScopeAction>()

        actions.forEach {
            when (it) {
                is ScopeAction.Fixture -> {
                    when (it.type) {
                        ScopeAction.FixtureType.BEFORE_GROUP -> beforeGroup.add(it)
                        ScopeAction.FixtureType.AFTER_GROUP -> afterGroup.add(it)
                        ScopeAction.FixtureType.BEFORE_EACH_TEST -> beforeEachTest.add(it)
                        ScopeAction.FixtureType.AFTER_EACH_TEST -> afterEachTest.add(it)
                    }
                }
                is ScopeAction.Value<*> -> {
                    when (it.cachingMode) {
                        CachingMode.GROUP, CachingMode.EACH_GROUP, CachingMode.SCOPE -> {
                            beforeGroup.add(it)
                            afterGroup.add(it)
                        }
                        CachingMode.TEST -> {
                            beforeEachTest.add(it)
                            afterEachTest.add(it)
                        }
                        else -> throw IllegalArgumentException("Invalid caching mode: ${it.cachingMode}")
                    }
                }
            }
        }

        return ExecutionUnit(
            beforeGroup,
            afterGroup,
            beforeEachTest,
            afterEachTest,
            tests
        )
    }
}

class ExecutionPlanner(private val fixturesAdapter: FixturesAdapter) {
    fun plan(req: ExecutionRequest): List<ExecutionUnit> {
        req.roots.forEach(this::doPlan)
        TODO()
    }

    private fun doPlan(scope: ScopeImpl) {
        doPlan(scope, ExecutionUnitTemplate())
    }

    private fun doPlan(scope: ScopeImpl, template: ExecutionUnitTemplate) {
        when {
            scope is GroupScopeImpl -> {
                val isFailFast = true

                if (isFailFast) {
                    // yield unit, fail fast should only contain test scopes as children
                } else {
                    template.addAction(ScopeAction.GroupStart(scope))
                    template.addActions(scope.getActions())

                    scope.getChildren().forEach { doPlan(it, template) }
                }
            }
            scope is TestScopeImpl -> {
                // yield unit
                scope.body
            }
        }
    }
}
