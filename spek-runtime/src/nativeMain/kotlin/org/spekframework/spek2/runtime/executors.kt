package org.spekframework.spek2.runtime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun doRunBlocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking {
        block()
    }
}

actual class TaskRunner actual constructor(concurrency: Int) {
    actual fun runTask(test: suspend () -> Unit): TaskHandle {
        doRunBlocking {
            test()
        }

        return object : TaskHandle {
            override fun await() {
                // do nothing
            }
        }
    }
}