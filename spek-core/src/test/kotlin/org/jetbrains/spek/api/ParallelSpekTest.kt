package org.jetbrains.spek.api

import java.util.Collections.nCopies
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class ParallelSpek : Spek({
    given("some parallel operations") {
        val n = 8
        val executorService = Executors.newFixedThreadPool(n)
        val callable = Callable {
            on("execution") {
                it("should something") {}
                it("should something else") {}
                it("should yet something else") { }
            }
        }
        executorService.invokeAll(nCopies(n, callable))
    }
})