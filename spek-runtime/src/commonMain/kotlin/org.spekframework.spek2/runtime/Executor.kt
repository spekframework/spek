package org.spekframework.spek2.runtime

import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

class Executor {

    fun execute(request: ExecutionRequest) {
        request.executionListener.executionStart()
        request.roots.forEach { execute(it, request.executionListener) }
        request.executionListener.executionFinish()
    }

    private fun execute(scope: ScopeImpl, listener: ExecutionListener) {
        if (scope.skip is Skip.Yes) {
            scopeIgnored(scope, scope.skip.reason, listener)
        } else {
            scopeExecutionStarted(scope, listener)

            val result = executeSafely {
                try {
                    scope.before()
                    scope.execute()

                    if (scope is GroupScopeImpl) {
                        scope.getChildren().forEach { execute(it, listener) }
                    }
                } finally {
                    scope.after()
                }
            }

            scopeExecutionFinished(scope, result, listener)
        }
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
