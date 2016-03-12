package org.jetbrains.spek.api

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.test.assertEquals

/**
 * Created by jakub on 30/01/16.
 */
@RunWith(JUnitParamsRunner::class)
class BeforeAfterEachTest {

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun beforeAfterEachOrder(runner: SpekTestCaseRunner) {
        val log = ArrayList<String>()
        runner.runTest({
            beforeEach { log.add("beforeEach1") }
            afterEach { log.add("afterEach1") }
            given("1") {
                log.add("given 1")
                beforeOn { log.add("beforeOn") }
                afterOn { log.add("afterOn") }
                on("1.1") {
                    log.add("on 1.1")
                    it("1.1") { log.add("it 1.1.1") }
                    it("1.2") { log.add("it 1.1.2") }
                }
                on("1.2") {
                    log.add("on 1.2")
                    it("2.1") { log.add("it 1.2.1") }
                    it("2.2") { log.add("it 1.2.2") }
                }
            }
            given("2") {
                log.add("given 2")
                on("2.1") {
                    log.add("on 2.1")
                    it ("1") { log.add("it 2.1.1") }
                }
            }

            beforeEach { log.add("beforeEach2") }
            afterEach { log.add("afterEach2") }
        })
        assertEquals(arrayListOf(
                "given 1",
                "beforeEach1",
                "beforeEach2",
                "beforeOn",
                "on 1.1",
                "it 1.1.1",
                "it 1.1.2",
                "afterOn",
                "afterEach1",
                "afterEach2",
                "beforeEach1",
                "beforeEach2",
                "beforeOn",
                "on 1.2",
                "it 1.2.1",
                "it 1.2.2",
                "afterOn",
                "afterEach1",
                "afterEach2",
                "given 2",
                "beforeEach1",
                "beforeEach2",
                "on 2.1",
                "it 2.1.1",
                "afterEach1",
                "afterEach2"
        ), log)
    }
}