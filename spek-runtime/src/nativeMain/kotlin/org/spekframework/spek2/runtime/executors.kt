package org.spekframework.spek2.runtime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun doRunBlocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking {
        block()
    }
}

actual class TestRunner actual constructor(concurrency: Int) {
    actual fun runTest(test: suspend () -> Unit): TestHandle {
        doRunBlocking {
            test()
        }

        return object : TestHandle {
            override fun await() {
                // do nothing
            }
        }
    }
}