package org.spekframework.spek2.runtime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withTimeout
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.lifecycle.ExecutionResult
import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionRequest
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

            fun finalize(result: ExecutionResult) {
                scope.after(result)

                when (scope) {
                    is GroupScopeImpl -> scope.invokeAfterGroupFixtures(false)
                    is TestScopeImpl -> scope.invokeAfterTestFixtures()
                }
            }

            val result = executeSafely(::finalize) {
                when (scope) {
                    is GroupScopeImpl -> {
                        scope.invokeBeforeGroupFixtures(false)
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
                                scope.invokeBeforeTestFixtures()
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
            }

            scopeExecutionFinished(scope, result, listener)

            return result
        }
    }

    private fun executeSafely(finalize: (ExecutionResult) -> Unit, block: () -> Unit): ExecutionResult {
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
