package org.spekframework.runtime

import org.jetbrains.spek.api.dsl.Pending
import org.spekframework.runtime.execution.ExecutionContext
import org.spekframework.runtime.execution.ExecutionResult
import org.spekframework.runtime.scope.ActionScopeImpl
import org.spekframework.runtime.scope.GroupScopeImpl
import org.spekframework.runtime.scope.ScopeImpl
import org.spekframework.runtime.scope.TestScopeImpl

class Executor {
    fun execute(context: ExecutionContext) {
        context.runtimeExecutionListener.executionStart()
        context.request.roots.forEach { execute(it, context) }
        context.runtimeExecutionListener.executionFinish()
    }

    private fun execute(scope: ScopeImpl, context: ExecutionContext) {
        if (scope.pending is Pending.Yes) {
            scopeIgnored(scope, scope.pending.reason, context)
        } else {
            scopeExecutionStarted(scope, context)
            val result = executeSafely {
                try {
                    scope.before(context)
                    scope.execute(context)

                    if (scope is GroupScopeImpl && scope !is ActionScopeImpl) {
                        scope.getChildren().forEach { execute(it, context) }
                    }
                } finally {
                    scope.after(context)
                }
            }
            scopeExecutionFinished(scope, result, context)
        }
    }

    private fun scopeExecutionStarted(scope: ScopeImpl, context: ExecutionContext) {
        when (scope) {
            is ActionScopeImpl -> context.runtimeExecutionListener.actionExecutionStart(scope)
            is GroupScopeImpl -> context.runtimeExecutionListener.groupExecutionStart(scope)
            is TestScopeImpl -> context.runtimeExecutionListener.testExecutionStart(scope)
        }
    }

    private fun scopeExecutionFinished(scope: ScopeImpl, result: ExecutionResult, context: ExecutionContext) {
        when (scope) {
            is ActionScopeImpl -> context.runtimeExecutionListener.actionExecutionFinish(scope, result)
            is GroupScopeImpl -> context.runtimeExecutionListener.groupExecutionFinish(scope, result)
            is TestScopeImpl -> context.runtimeExecutionListener.testExecutionFinish(scope, result)
        }
    }

    private fun scopeIgnored(scope: ScopeImpl, reason: String?, context: ExecutionContext) {
        when (scope) {
            is ActionScopeImpl -> context.runtimeExecutionListener.actionIgnored(scope, reason)
            is GroupScopeImpl -> context.runtimeExecutionListener.groupIgnored(scope, reason)
            is TestScopeImpl -> context.runtimeExecutionListener.testIgnored(scope, reason)
        }
    }

    private inline fun executeSafely(block: () -> Unit): ExecutionResult {
        return try {
            block()
            ExecutionResult.Success
        } catch (e: Throwable) {
            ExecutionResult.Failure(e)
        }
    }
}
