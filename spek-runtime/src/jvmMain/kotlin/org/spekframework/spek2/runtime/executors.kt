package org.spekframework.spek2.runtime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

actual fun doRunBlocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking {
        block()
    }
}

actual class TestRunner actual constructor(concurrency: Int) {
    private val executor = Executors.newFixedThreadPool(concurrency) { r ->
        Thread(r).also {
            it.isDaemon = true
        }
    }

    actual fun runTest(test: suspend () -> Unit): TestHandle {
        val handle = CompletableFuture<Unit>()

        executor.submit {
            runBlocking {
                test()
                handle.complete(null)
            }
        }

        return object : TestHandle {
            override fun await() {
                try {
                handle.get()
                } catch (e: ExecutionException) {
                    throw e.cause!!
                }
            }
        }
    }
}