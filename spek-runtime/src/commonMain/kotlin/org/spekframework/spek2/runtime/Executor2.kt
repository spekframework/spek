package org.spekframework.spek2.runtime

import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueReader
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeDeclaration
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl


class Executor2 {
    private class ExecutionPhases(val beforeGroup: List<ScopeDeclaration>,
                                  val afterGroup: List<ScopeDeclaration>,
                                  val beforeEachGroup: List<ScopeDeclaration>,
                                  val afterEachGroup: List<ScopeDeclaration>,
                                  val beforeEachTest: List<ScopeDeclaration>,
                                  val afterEachTest: List<ScopeDeclaration>)

    fun execute(request: ExecutionRequest) {
        request.executionListener.executionStart()
        request.roots.forEach { execute(it as GroupScopeImpl, request.executionListener, emptyList(), emptyList(), emptyList(), emptyList()) }
        request.executionListener.executionFinish()
    }

    private fun execute(group: GroupScopeImpl, listener: ExecutionListener,
                        beforeEachGroup: List<ScopeDeclaration>, afterEachGroup: List<ScopeDeclaration>,
                        beforeEachTest: List<ScopeDeclaration>, afterEachTest: List<ScopeDeclaration>) {
        if (group.skip is Skip.Yes) {
            scopeIgnored(group, group.skip.reason, listener)
            return
        }

        scopeExecutionStarted(group, listener)
        val phases = extractPhases(group.getDeclarations())
        val combinedBeforeEachGroup = beforeEachGroup + phases.beforeEachGroup
        val combinedAfterEachGroup = phases.afterEachGroup + afterEachGroup
        val combinedBeforeEachTest = beforeEachTest + phases.beforeEachTest
        val combinedAfterEachTest = phases.afterEachTest + afterEachTest
        val result = executeSafely {
            try {
                executeBeforeGroup(phases.beforeGroup)
                executeBeforeEachGroup(combinedBeforeEachGroup)
                if (group.failFast) {
                    // fail fast group should only contain tests
                    val tests = group.getChildren().map { it as TestScopeImpl }

                    var failed = false
                    for (test in tests) {
                        if (failed) {
                            scopeIgnored(test, "Previous test failed", listener)
                            continue
                        }

                        failed = executeTest(test, listener, combinedBeforeEachTest, combinedAfterEachTest) != ExecutionResult.Success
                    }

                } else {
                    group.getChildren().forEach { child ->
                        when (child) {
                            is GroupScopeImpl -> execute(child, listener, combinedBeforeEachGroup, combinedAfterEachGroup, combinedBeforeEachTest, combinedAfterEachTest)
                            is TestScopeImpl -> {
                                executeTest(child, listener, combinedBeforeEachTest, combinedAfterEachTest)
                            }
                        }
                    }
                }
            } finally {
                executeAfterEachGroup(combinedAfterEachGroup)
                executeAfterGroup(phases.afterGroup)
            }
        }

        scopeExecutionFinished(group, result, listener)
    }

    private fun executeTest(test: TestScopeImpl, listener: ExecutionListener,
                            beforeEachTest: List<ScopeDeclaration>, afterEachTest: List<ScopeDeclaration>): ExecutionResult? {
        if (test.skip is Skip.Yes) {
            scopeIgnored(test, test.skip.reason, listener)
            return null
        }

        scopeExecutionStarted(test, listener)
        val result = executeSafely {
            try {
                executeBeforeEachTest(beforeEachTest)
                test.body(object: TestBody {
                    override fun <T> memoized(): MemoizedValue<T> {
                        return MemoizedValueReader(test)
                    }

                })
            } finally {
                executeAfterEachTest(afterEachTest)
            }
        }
        scopeExecutionFinished(test, result, listener)
        return result
    }

    private fun executeBeforeEachTest(beforeEachTest: List<ScopeDeclaration>) {
        beforeEachTest.forEach {
            when (it) {
                is ScopeDeclaration.Fixture -> it.cb()
                is ScopeDeclaration.Memoized<*> -> {
                    it.adapter.init()
                }
            }
        }
    }

    private fun executeAfterEachTest(afterEachTest: List<ScopeDeclaration>) {
        afterEachTest.forEach {
            when (it) {
                is ScopeDeclaration.Fixture -> it.cb()
                is ScopeDeclaration.Memoized<*> -> {
                    it.adapter.destroy()
                }
            }
        }
    }

    private fun executeBeforeGroup(beforeGroup: List<ScopeDeclaration>) {
        beforeGroup.forEach {
            when (it) {
                is ScopeDeclaration.Fixture -> it.cb()
                is ScopeDeclaration.Memoized<*> -> {
                    it.adapter.init()
                }
            }
        }
    }

    private fun executeAfterGroup(afterGroup: List<ScopeDeclaration>) {
        afterGroup.forEach {
            when (it) {
                is ScopeDeclaration.Fixture -> it.cb()
                is ScopeDeclaration.Memoized<*> -> {
                    it.adapter.destroy()
                }
            }
        }
    }

    private fun executeBeforeEachGroup(beforeEachGroup: List<ScopeDeclaration>) {
        beforeEachGroup.forEach {
            when (it) {
                is ScopeDeclaration.Fixture -> it.cb()
                is ScopeDeclaration.Memoized<*> -> {
                    it.adapter.init()
                }
            }
        }
    }

    private fun executeAfterEachGroup(afterEachGroup: List<ScopeDeclaration>) {
        afterEachGroup.forEach {
            when (it) {
                is ScopeDeclaration.Fixture -> it.cb()
                is ScopeDeclaration.Memoized<*> -> {
                    it.adapter.destroy()
                }
            }
        }
    }

    private fun extractPhases(declarations: List<ScopeDeclaration>): ExecutionPhases {
        val beforeGroup = mutableListOf<ScopeDeclaration>()
        val afterGroup = mutableListOf<ScopeDeclaration>()
        val beforeEachGroup = mutableListOf<ScopeDeclaration>()
        val afterEachGroup = mutableListOf<ScopeDeclaration>()
        val beforeEachTest = mutableListOf<ScopeDeclaration>()
        val afterEachTest = mutableListOf<ScopeDeclaration>()

        declarations.forEach {
            when (it) {
                is ScopeDeclaration.Fixture -> {
                    when (it.type) {
                        ScopeDeclaration.FixtureType.BEFORE_GROUP -> beforeGroup.add(it)
                        ScopeDeclaration.FixtureType.AFTER_GROUP -> afterGroup.add(it)
                        ScopeDeclaration.FixtureType.BEFORE_EACH_GROUP -> beforeEachGroup.add(it)
                        ScopeDeclaration.FixtureType.AFTER_EACH_GROUP -> afterEachGroup.add(it)
                        ScopeDeclaration.FixtureType.BEFORE_EACH_TEST -> beforeEachTest.add(it)
                        ScopeDeclaration.FixtureType.AFTER_EACH_TEST -> afterEachTest.add(it)
                        else -> throw IllegalArgumentException("Invalid fixture type: ${it.type}")
                    }
                }
                is ScopeDeclaration.Memoized<*> -> {
                    when (it.cachingMode) {
                        CachingMode.GROUP, CachingMode.EACH_GROUP -> {
                            beforeGroup.add(it)
                            afterGroup.add(it)
                        }
                        CachingMode.SCOPE -> {
                            beforeEachGroup.add(it)
                            afterEachGroup.add(it)
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

        return ExecutionPhases(
            beforeGroup, afterGroup, beforeEachGroup, afterEachGroup, beforeEachTest, afterEachTest
        )
    }

    private inline fun executeSafely(block: () -> Unit): ExecutionResult = try {
        block()
        ExecutionResult.Success
    } catch (e: Throwable) {
        ExecutionResult.Failure(e)
    }

    private fun scopeExecutionStarted(scope: ScopeImpl, listener: ExecutionListener) =
        when (scope) {
            is GroupScopeImpl -> listener.groupExecutionStart(scope)
            is TestScopeImpl -> listener.testExecutionStart(scope)
        }

    private fun scopeExecutionFinished(scope: ScopeImpl, result: ExecutionResult, listener: ExecutionListener) =
        when (scope) {
            is GroupScopeImpl -> listener.groupExecutionFinish(scope, result)
            is TestScopeImpl -> listener.testExecutionFinish(scope, result)
        }

    private fun scopeIgnored(scope: ScopeImpl, reason: String?, listener: ExecutionListener) =
        when (scope) {
            is GroupScopeImpl -> listener.groupIgnored(scope, reason)
            is TestScopeImpl -> listener.testIgnored(scope, reason)
        }
}
