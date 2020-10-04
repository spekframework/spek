package org.spekframework.spek2.runtime

interface TaskHandle {
    fun await()
}

expect class TaskRunner(concurrency: Int) {
    fun runTask(test: suspend () -> Unit): TaskHandle
}