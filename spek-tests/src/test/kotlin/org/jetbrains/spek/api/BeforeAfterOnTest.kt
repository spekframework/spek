package org.jetbrains.spek.api

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.fail

@RunWith(JUnitParamsRunner::class)
public class BeforeAfterOnTest {

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    public fun callOnBefore(runner: SpekTestCaseRunner) {
        val log = arrayListOf<String>()
        runner.runTest({
            given("a") {
                beforeOn { log.add("before") }
                on("1") {
                    it("ddd") {}
                    log.add("on")
                }

            }
        })
        assertEquals(arrayListOf("before", "on"), log)
    }

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    public fun callOnAfter(runner: SpekTestCaseRunner) {
        val log = arrayListOf<String>()
        runner.runTest({
            given("a") {
                afterOn { log.add("after") }
                on("1") {
                    it("ddd") {}
                    log.add("on")
                }

            }
        })
        assertEquals(arrayListOf("on", "after"), log)
    }

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    public fun callOnBeforeAndOnAfter(runner: SpekTestCaseRunner) {
        val log = arrayListOf<String>()
        runner.runTest({
            given("1") {
                log.add("given1.start")
                beforeOn { log.add("before") }
                afterOn { log.add("after") }

                on("1") {
                    log.add("on1.start")
                    it("1") { log.add("on1.it1") }
                    it("2") { log.add("on1.it2") }
                    log.add("on1.end")
                }
                on("2") {
                    log.add("on2.start")
                    it("1") {
                        log.add("on2.it1");
                        fail("rrr")
                    }
                    it("2") { log.add("on2.it2") }
                    log.add("on2.end")
                }
                log.add("given1.end")

            }
        })

        val expected = arrayListOf(
                "given1.start",
                "given1.end",
                "before",
                "on1.start",
                "on1.end",
                "on1.it1",
                "on1.it2",
                "after",
                "before",
                "on2.start",
                "on2.end",
                "on2.it1",
                "on2.it2",
                "after")

        Assert.assertEquals(expected, log)
    }

}