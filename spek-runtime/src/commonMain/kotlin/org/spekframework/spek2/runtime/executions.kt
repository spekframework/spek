package org.spekframework.spek2.runtime

import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeDeclaration
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

interface ExecutionContext

class ExecutionUnit(
    private val beforeGroup: MutableList<ScopeDeclaration>,
    private val afterGroup: MutableList<ScopeDeclaration>,
    private val beforeEachTest: MutableList<ScopeDeclaration>,
    private val afterEachTest: MutableList<ScopeDeclaration>,
    private val tests: MutableList<TestBody.() -> Unit>
) {
    fun execute(context: ExecutionContext) {
        try {
            // before group fixtures and declare scope values (group & scope caching modes) in order
            beforeGroup.forEach {
                when (it) {
                    is ScopeDeclaration.Fixture -> it.cb()
                    is ScopeDeclaration.Memoized<*> -> {
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
                            is ScopeDeclaration.Fixture -> it.cb()
                            is ScopeDeclaration.Memoized<*> -> {
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
                            is ScopeDeclaration.Fixture -> it.cb()
                            is ScopeDeclaration.Memoized<*> -> {
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
                    is ScopeDeclaration.Fixture -> it.cb()
                    is ScopeDeclaration.Memoized<*> -> {
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
    private val actions = mutableListOf<ScopeDeclaration>()
    private val tests = mutableListOf<TestBody.() -> Unit>()

    fun addAction(declaration: ScopeDeclaration): ExecutionUnitTemplate {
        this.actions.add(declaration)
        return this
    }

    fun addActions(declarations: List<ScopeDeclaration>): ExecutionUnitTemplate {
        this.actions.addAll(declarations)
        return this
    }

    fun build(): ExecutionUnit {
        val beforeGroup = mutableListOf<ScopeDeclaration>()
        val afterGroup = mutableListOf<ScopeDeclaration>()
        val beforeEachTest = mutableListOf<ScopeDeclaration>()
        val afterEachTest = mutableListOf<ScopeDeclaration>()

        actions.forEach {
            when (it) {
                is ScopeDeclaration.Fixture -> {
                    when (it.type) {
                        ScopeDeclaration.FixtureType.BEFORE_GROUP -> beforeGroup.add(it)
                        ScopeDeclaration.FixtureType.AFTER_GROUP -> afterGroup.add(it)
                        ScopeDeclaration.FixtureType.BEFORE_EACH_TEST -> beforeEachTest.add(it)
                        ScopeDeclaration.FixtureType.AFTER_EACH_TEST -> afterEachTest.add(it)
                    }
                }
                is ScopeDeclaration.Memoized<*> -> {
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
                    template.addActions(scope.getDeclarations())

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
