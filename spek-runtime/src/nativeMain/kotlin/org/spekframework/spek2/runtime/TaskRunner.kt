package org.spekframework.spek2.runtime

actual class TaskRunner actual constructor(concurrency: Int) {
    actual fun runTask(test: suspend () -> Unit): TaskHandle {
        doRunBlocking {
            test()
        }

        return object: TaskHandle {
            override fun await() {
                // nada
            }
        }
    }
}