package org.spekframework.spek2.jvm

import org.spekframework.spek2.lifecycle.Scope
import org.spekframework.spek2.runtime.execution.ExecutionResult

sealed class ExecutionEvent(val scope: Scope) {
    class Ignored(scope: Scope, val reason: String?): ExecutionEvent(scope)
    class Started(scope: Scope): ExecutionEvent(scope)
    class Finished(scope: Scope, val result: ExecutionResult): ExecutionEvent(scope)
}
