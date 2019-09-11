package org.spekframework.spek2.lifecycle

sealed class ExecutionResult {
    object Success : ExecutionResult()
    class Failure(val cause: Throwable) : ExecutionResult()
}
