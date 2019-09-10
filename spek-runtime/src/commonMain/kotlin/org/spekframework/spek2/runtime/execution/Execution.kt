package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl
import org.spekframework.spek2.lifecycle.ExecutionResult as PublicExecutionResult

data class ExecutionRequest(val roots: List<ScopeImpl>, val executionListener: ExecutionListener)

sealed class ExecutionResult {
    object Success : ExecutionResult()
    class Failure(val cause: Throwable) : ExecutionResult()

    // introduced to avoid breaking binary compatibility
    fun toPublicExecutionResult(): PublicExecutionResult {
        return when(this) {
            is Success -> PublicExecutionResult.Success
            is Failure -> PublicExecutionResult.Failure(this.cause)
        }
    }
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
}
