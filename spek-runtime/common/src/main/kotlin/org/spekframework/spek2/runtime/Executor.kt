package org.spekframework.spek2.runtime

import org.spekframework.spek2.dsl.Pending
import org.spekframework.spek2.runtime.execution.ExecutionContext
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.ActionScopeImpl
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

class Executor {
    fun execute(context: ExecutionContext) {
        context.executionListener.executionStart()
        context.request.roots.forEach { execute(it, context) }
        context.executionListener.executionFinish()
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
            is ActionScopeImpl -> context.executionListener.actionExecutionStart(scope)
            is GroupScopeImpl -> context.executionListener.groupExecutionStart(scope)
            is TestScopeImpl -> context.executionListener.testExecutionStart(scope)
        }
    }

    private fun scopeExecutionFinished(scope: ScopeImpl, result: ExecutionResult, context: ExecutionContext) {
        when (scope) {
            is ActionScopeImpl -> context.executionListener.actionExecutionFinish(scope, result)
            is GroupScopeImpl -> context.executionListener.groupExecutionFinish(scope, result)
            is TestScopeImpl -> context.executionListener.testExecutionFinish(scope, result)
        }
    }

    private fun scopeIgnored(scope: ScopeImpl, reason: String?, context: ExecutionContext) {
        when (scope) {
            is ActionScopeImpl -> context.executionListener.actionIgnored(scope, reason)
            is GroupScopeImpl -> context.executionListener.groupIgnored(scope, reason)
            is TestScopeImpl -> context.executionListener.testIgnored(scope, reason)
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
