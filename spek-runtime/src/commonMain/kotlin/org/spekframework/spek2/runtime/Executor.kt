package org.spekframework.spek2.runtime

import kotlinx.coroutines.*
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class Executor {
    suspend fun execute(request: ExecutionRequest) {
        request.executionListener.executionStart()
        // note that this call will be run in parallel depending on the CoroutineDispatcher used
        supervisorScope {
            request.roots.map { async { execute(it, request.executionListener) } }
                .forEach { job ->
                    try {
                        job.await()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
        }
        request.executionListener.executionFinish()
    }

    private suspend fun execute(scope: ScopeImpl, listener: ExecutionListener): ExecutionResult? {
        if (scope.skip is Skip.Yes) {
            scopeIgnored(scope, scope.skip.reason, listener)
            return null
        } else {
            scopeExecutionStarted(scope, listener)

            suspend fun finalize(result: ExecutionResult) {
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

            val scopeCoroutineContext: CoroutineContext = EmptyCoroutineContext
            val result = executeSafely(scopeCoroutineContext, { finalize(it) }) {
                when (scope) {
                    is GroupScopeImpl -> {
                        withContext(scopeCoroutineContext) {
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
                        val exception = withContext(scopeCoroutineContext) {
                            val job = async {
                                scope.before()
                                scope.invokeBeforeTestFixtures()
                                scope.execute()
                            }

                            if (scope.timeout == 0L) {
                                try {
                                    job.await()
                                    null
                                } catch (e: Throwable) {
                                    e
                                }
                            } else {
                                val timedExecutionResult = withTimeoutOrNull(scope.timeout) {
                                    try {
                                        job.await()
                                        TimedExecutionResult.Success
                                    } catch (e: Throwable) {
                                        TimedExecutionResult.Failed(e)
                                    }
                                }

                                if (timedExecutionResult == null) {
                                    // test may still be running, cancel it!
                                    job.cancel()
                                    TestScopeTimeoutException(scope)
                                } else {
                                    when (timedExecutionResult) {
                                        is TimedExecutionResult.Failed -> timedExecutionResult.exception
                                        is TimedExecutionResult.Success -> null
                                        else -> throw AssertionError("Unsupported TimedExecutionResult: $timedExecutionResult")
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

    private sealed class TimedExecutionResult {
        object Success : TimedExecutionResult()
        class Failed(val exception: Throwable) : TimedExecutionResult()
    }

    private class TestScopeTimeoutException(scopeImpl: TestScopeImpl) : Throwable(
        "Execution of test ${scopeImpl.path.name} has timed out!"
    )

    private suspend fun executeSafely(coroutineContext: CoroutineContext, finalize: suspend (ExecutionResult) -> Unit, block: suspend () -> Unit): ExecutionResult {
        val result = try {
            block()
            ExecutionResult.Success
        } catch (e: Throwable) {
            ExecutionResult.Failure(e)
        }

        // failures here will replace execution result
        return try {
            withContext(coroutineContext) {
                finalize(result)
            }
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
