package org.spekframework.spek2

import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runBlockingTest
import org.spekframework.spek2.style.specification.describe

object TimeoutTest: AbstractSpekTest({ helper ->
    describe("test timeouts") {
        it("should timeout using default settings", timeout = 0) {
            runBlockingTest {
                val latch = Channel<Int>()
                val recorder = async {
                    helper.executeTest(testData.timeoutTest.DefaultTimeoutTest(latch))
                }
                // should timeout
                advanceTimeBy(10000)
                // should *not* timeout
                advanceTimeBy(9000)
                latch.send(0)

                // timeout specification style
                // should timeout
                advanceTimeBy(10000)
                // should *not* timeout
                advanceTimeBy(9000)
                latch.send(0)


                // timeout gherkin style
                // should timeout
                advanceTimeBy(10000)
                // should *not* timeout
                advanceTimeBy(9000)
                latch.send(0)


                helper.assertExecutionEquals(
                    recorder.await().events()
                ) {
                    group("DefaultTimeoutTest") {
                        test("should timeout", false)
                        test("should not timeout", true)
                        group("timeout specification style") {
                            test("should timeout", false)
                            test("should not timeout", true)
                        }
                        group("Feature: timeout gherkin style") {
                            group("Scenario: timeout scenario") {
                                test("Then: should timeout", false)
                            }
                            group("Scenario: not timeout scenario") {
                                test("Then: should not timeout", true)
                            }
                        }
                    }
                }
            }
        }
    }

    describe("custom timeouts") {
        it("should timeout using specified settings") {
            val recorder = helper.executeTest(testData.timeoutTest.CustomTimeoutTest)

            helper.assertExecutionEquals(
                recorder.events()
            ) {
                group("CustomTimeoutTest") {
                    test("should timeout", false)
                    group("timeout specification style") {
                        test("should timeout", false)
                    }
                    group("Feature: timeout gherkin style") {
                        group("Scenario: some scenario") {
                            test("Then: should timeout", false)
                        }
                    }
                }
            }
        }
    }

    describe("global timeouts") {
        it("should use specified global timeout", timeout = 0) {
            System.setProperty("SPEK_TIMEOUT", 15000L.toString())
            val recorder = helper.executeTest(testData.timeoutTest.GlobalTimeoutTest)

            helper.assertExecutionEquals(
                recorder.events()
            ) {
                group("GlobalTimeoutTest") {
                    test("this should run for 10 seconds and pass since global timeout is 20")
                }
            }
            System.clearProperty("SPEK_TIMEOUT")
        }
    }
})
