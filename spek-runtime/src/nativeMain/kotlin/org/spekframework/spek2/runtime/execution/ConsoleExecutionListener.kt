package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

class ConsoleExecutionListener : ExecutionListener {
    private var totalTests = 0
    private var passedTests = 0
    private var failedTests = 0
    private var ignoredTests = 0
    private var ignoredGroups = 0

    override fun executionStart() {
        println("START: execution")
    }

    override fun executionFinish() {
        println("FINISH: execution: $totalTests tests, $passedTests passed, $failedTests failed, $ignoredTests skipped. $ignoredGroups groups were skipped.")
    }

    override fun testExecutionStart(test: TestScopeImpl) {
        println("START: test: ${test.path}")
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) =
            when (result) {
                is ExecutionResult.Success -> {
                    totalTests++
                    passedTests++

                    println("PASSED: test: ${test.path}")
                }
                is ExecutionResult.Failure -> {
                    totalTests++
                    failedTests++

                    println("FAILED: test: ${test.path}: ${result.cause}")
                }
            }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        totalTests++
        ignoredTests++

        println("IGNORED: test: ${test.path}: ${reason ?: "<no reason given>"}")
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        println("START: group: ${group.path}")
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) =
            when (result) {
                is ExecutionResult.Success -> println("PASSED: group: ${group.path}")
                is ExecutionResult.Failure -> println("FAILED: group: ${group.path}: ${result.cause}")
            }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        ignoredGroups++

        println("IGNORED: group: ${group.path}: ${reason ?: "<no reason given>"}")
    }

    val wasSuccessful: Boolean
        get() = failedTests == 0
}
