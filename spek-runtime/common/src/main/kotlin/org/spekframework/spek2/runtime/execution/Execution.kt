package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.scope.ActionScopeImpl
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

sealed class ExecutionResult {
    object Success: ExecutionResult()
    class Failure(val cause: Throwable): ExecutionResult()
}

interface ExecutionListener {
    fun executionStart()
    fun executionFinish()

    fun testExecutionStart(test: TestScopeImpl)
    fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult)
    fun testIgnored(test: TestScopeImpl, reason: String?)

    fun groupExecutionStart(group: GroupScopeImpl)
    fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult)
    fun groupIgnored(group: GroupScopeImpl, reason: String?)

    fun actionExecutionStart(action: ActionScopeImpl)
    fun actionExecutionFinish(action: ActionScopeImpl, result: ExecutionResult)
    fun actionIgnored(action: ActionScopeImpl, reason: String?)
}

abstract class RuntimeExecutionListener: ExecutionListener {
    open fun dynamicTestRegistered(test: TestScopeImpl, context: ExecutionContext) {
        testExecutionStart(test)
        try {
            test.execute(context)
            testExecutionFinish(test, ExecutionResult.Success)
        } catch (e: Throwable) {
            testExecutionFinish(test, ExecutionResult.Failure(e))
        }
    }
}

data class ExecutionRequest(val roots: List<ScopeImpl>, val runtimeExecutionListener: RuntimeExecutionListener)

data class ExecutionContext(val request: ExecutionRequest) {
    val runtimeExecutionListener = request.runtimeExecutionListener
}
