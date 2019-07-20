package org.spekframework.spek2.runtime

import kotlinx.coroutines.*
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

    private fun execute(scope: ScopeImpl, listener: ExecutionListener): ExecutionResult? {
        if (scope.skip is Skip.Yes) {
            scopeIgnored(scope, scope.skip.reason, listener)
            return null
        } else {
            scopeExecutionStarted(scope, listener)

            val result = executeSafely {
                try {
                    when (scope) {
                        is GroupScopeImpl -> {
                            scope.before()
                            var failed = false
                            for (it in scope.getChildren()) {

                                if (failed) {
                                    scopeIgnored(it, "Previous failure detected, skipping.", listener)
                                    continue
                                }

                                val result = execute(it, listener)
                                if (scope.failFast && it is TestScopeImpl && result is ExecutionResult.Failure) {
                                    failed = true
                                }
                            }
                        }
                        is TestScopeImpl -> {
                            doRunBlocking {
                                // this needs to be here, in K/N the event loop
                                // is started during a runBlocking call. Calling
                                // any builders outside that will throw an exception.
                                val job = GlobalScope.async {
                                    scope.before()
                                    scope.execute()
                                }

                                val exception = withTimeout(scope.timeout) {
                                    try {
                                        job.await()
                                        null
                                    } catch (e: Throwable) {
                                        e
                                    }
                                }

                                if (exception != null) {
                                    throw exception
                                }
                            }
                        }
                    }
                } finally {
                    scope.after()
                }
            }

            scopeExecutionFinished(scope, result, listener)

            return result
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

expect fun doRunBlocking(block: suspend CoroutineScope.() -> Unit)
