package org.jetbrains.spek.api

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(JUnitParamsRunner::class)
class ExecutionOrderTest {

    /**
     * Issue #39 test
     * @see https://github.com/JetBrains/spek/issues/39
     */
    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun executionOrderWithBeforeAfter(runner: SpekTestCaseRunner) {
        val log = arrayListOf<String>()
        runner.runTest({
            given("") {
                beforeOn { log.add("1") }
                on("") {
                    log.add("2")
                    it("") {
                        log.add("3")
                    }
                }
                afterOn { log.add("4") }
            }
        })
        assertEquals(arrayListOf("1", "2", "3", "4"), log)
    }
}