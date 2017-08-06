package org.spekframework.runtime.test.event

import org.jetbrains.spek.api.lifecycle.Scope
import org.spekframework.runtime.execution.ExecutionResult

/**
 * @author Ranie Jade Ramiso
 */
sealed class ExecutionEvent(val scope: Scope) {
    class Ignored(scope: Scope, val reason: String?): ExecutionEvent(scope)
    class Started(scope: Scope): ExecutionEvent(scope)
    class Finished(scope: Scope, val result: ExecutionResult): ExecutionEvent(scope)
}
