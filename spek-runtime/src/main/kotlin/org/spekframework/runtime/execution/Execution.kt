package org.spekframework.runtime.execution

import org.spekframework.runtime.scope.Action
import org.spekframework.runtime.scope.Group
import org.spekframework.runtime.scope.Scope
import org.spekframework.runtime.scope.Test

class ExecutionRequest(val roots: List<Scope>)

sealed class ExecutionResult {
    object Success: ExecutionResult()
    class Failure(val reason: Throwable)
}

interface ExecutionListener {
    fun testExecutionStart(test: Test)
    fun testExecutionFinish(test: Test, result: ExecutionResult)
    fun testIgnored(test: Test, reason: String?)

    fun groupExecutionStart(group: Group)
    fun groupExecutionFinish(group: Group, result: ExecutionResult)
    fun groupIgnored(group: Group, reason: String?)

    fun actionExecutionStart(action: Action)
    fun actionExecutionFinish(action: Action, result: ExecutionResult)
    fun actionIgnored(action: Action, reason: String?)
}
