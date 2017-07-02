package org.spekframework.runtime.execution

import org.spekframework.runtime.scope.ActionScopeImpl
import org.spekframework.runtime.scope.GroupScopeImpl
import org.spekframework.runtime.scope.ScopeImpl
import org.spekframework.runtime.scope.TestScopeImpl

sealed class ExecutionResult {
    object Success: ExecutionResult()
    class Failure(val reason: Throwable): ExecutionResult()
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
    fun dynamicTestRegistered(test: TestScopeImpl, context: ExecutionContext) {
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
