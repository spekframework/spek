package org.spekframework.spek2

import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runBlockingTest
import org.spekframework.spek2.style.specification.describe

object TimeoutTest: AbstractSpekTest({ helper ->
    describe("test timeouts") {
        it("specification style timeouts") {
            runBlockingTest {
                val latch = Channel<Int>()
                val recorder = async {
                    helper.executeTest(testData.timeoutTest.SpecificationStyleTimeoutTests(latch))
                }

                advanceTimeBy(10_000)
                advanceTimeBy(300)
                advanceTimeBy(9_000)
                latch.send(0)
                advanceTimeBy(400)
                latch.send(0)

                helper.assertExecutionEquals(
                    recorder.await().events()
                ) {
                    group("SpecificationStyleTimeoutTests") {
                        group("timeouts") {
                            test("tests running pass 300ms should fail", false)
                            test("tests running less than default timeout should succeed")
                            test("tests running less than 500ms should succeed")
                        }
                    }
                }
            }
        }

        it("gherkin style timeouts") {
            runBlockingTest {
                val latch = Channel<Int>()
                val recorder = async {
                    helper.executeTest(testData.timeoutTest.GherkinStyleTimeoutTests(latch))
                }

                advanceTimeBy(10_000)
                advanceTimeBy(600)
                advanceTimeBy(9_000)
                latch.send(0)
                advanceTimeBy(1150)
                latch.send(0)

                helper.assertExecutionEquals(
                    recorder.await().events()
                ) {
                    group("GherkinStyleTimeoutTests") {
                        group("Feature: Timeouts") {
                            group("Scenario: Running more than 600ms") {
                                test("Then: It should fail", false)
                            }
                            group("Scenario: Running less than default") {
                                test("Then: It should succeed")
                            }
                            group("Scenario: Running less than 1200ms") {
                                test("Then: It should succeed")
                            }
                        }
                    }
                }
            }
        }

        describe("global timeouts") {
            it("should use specified global timeout") {
                runBlockingTest {
                    System.setProperty("SPEK_TIMEOUT", 15000L.toString())
                    val latch = Channel<Int>()
                    val recorder = async {
                        helper.executeTest(testData.timeoutTest.GlobalTimeoutTest(latch))
                    }

                    advanceTimeBy(14_500)
                    latch.send(0)

                    helper.assertExecutionEquals(
                        recorder.await().events()
                    ) {
                        group("GlobalTimeoutTest") {
                            test("this should run for 10s and pass since global timeout is 15s")
                        }
                    }
                    System.clearProperty("SPEK_TIMEOUT")
                }
            }
        }
    }
})
