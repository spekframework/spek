package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

class ConsoleExecutionListener : ExecutionListener {
    override fun executionStart() {
        println("START: execution")
    }

    override fun executionFinish() {
        println("FINISH: execution")
    }

    override fun testExecutionStart(test: TestScopeImpl) {
        println("START: test: ${test.path}")
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) =
            when (result) {
                is ExecutionResult.Success -> println("PASSED: test: ${test.path}")
                is ExecutionResult.Failure -> println("FAILED: test: ${test.path}: ${result.cause}")
            }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        println("IGNORED: test: ${test.path}: ${reason ?: "<no reason given>"}")
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        println("START: group: ${group.path}")
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult)  =
            when (result) {
                is ExecutionResult.Success -> println("PASSED: group: ${group.path}")
                is ExecutionResult.Failure -> println("FAILED: group: ${group.path}: ${result.cause}")
            }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        println("IGNORED: group: ${group.path}: ${reason ?: "<no reason given>"}")
    }
}
