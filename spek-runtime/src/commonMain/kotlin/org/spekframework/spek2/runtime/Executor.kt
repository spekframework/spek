package org.spekframework.spek2.runtime

import kotlinx.coroutines.*
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl
import kotlin.coroutines.EmptyCoroutineContext

class Executor {
    suspend fun execute(request: ExecutionRequest) {
        request.executionListener.executionStart()
        request.roots.forEach { execute(it, request.executionListener) }
        request.executionListener.executionFinish()
    }

    private suspend fun execute(scope: ScopeImpl, listener: ExecutionListener): ExecutionResult? {
        if (scope.skip is Skip.Yes) {
            scopeIgnored(scope, scope.skip.reason, listener)
            return null
        } else {
            scopeExecutionStarted(scope, listener)

            fun finalize(result: ExecutionResult) {
                val actualResult = try {
                    when (scope) {
                        is GroupScopeImpl -> scope.invokeAfterGroupFixtures(false)
                        is TestScopeImpl -> scope.invokeAfterTestFixtures()
                    }
                    result
                } catch (e: Throwable) {
                    ExecutionResult.Failure(e)
                }

                scope.after(actualResult.toPublicExecutionResult())

                if (actualResult is ExecutionResult.Failure) {
                    throw actualResult.cause
                }
            }

            val contextOverride = EmptyCoroutineContext
            val result = executeSafely({ finalize(it) }) {
                when (scope) {
                    is GroupScopeImpl -> {
                        withContext(contextOverride) {
                            scope.before()
                            scope.invokeBeforeGroupFixtures(false)
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
                    }
                    is TestScopeImpl -> {
                        val exception = withContext(contextOverride) {
                            val job = launch {
                                scope.before()
                                scope.invokeBeforeTestFixtures()
                                scope.execute()
                            }

                            if (scope.timeout == 0L) {
                                try {
                                    job.join()
                                    null
                                } catch (e: Throwable) {
                                    e
                                }
                            } else {
                                withTimeout(scope.timeout) {
                                    try {
                                        job.join()
                                        null
                                    } catch (e: Throwable) {
                                        e
                                    }
                                }
                            }
                        }

                        if (exception != null) {
                            throw exception
                        }
                    }
                }
            }

            scopeExecutionFinished(scope, result, listener)

            return result
        }
    }

    private suspend fun executeSafely(finalize: suspend (ExecutionResult) -> Unit, block: suspend () -> Unit): ExecutionResult {
        val result = try {
            block()
            ExecutionResult.Success
        } catch (e: Throwable) {
            ExecutionResult.Failure(e)
        }

        // failures here will replace execution result
        return try {
            finalize(result)
            result
        } catch (e: Throwable) {
            ExecutionResult.Failure(e)
        }
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
