package org.spekframework.spek2

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.spekframework.spek2.style.specification.describe

@OptIn(ExperimentalStdlibApi::class)
object TimeoutTest : AbstractSpekTest({ helper ->
    describe("test timeouts") {
        it("specification style timeouts") {
            val recorder = GlobalScope.async {
                helper.executeTest(testData.timeoutTest.SpecificationStyleTimeoutTests)
            }

            helper.assertExecutionEquals(
                    recorder.await().events()
            ) {
                group("SpecificationStyleTimeoutTests") {
                    group("timeouts") {
                        test("tests running pass 300ms should fail", false)
                        test("tests running less than 500ms should succeed")
                    }
                }
            }
        }

        it("gherkin style timeouts") {
            val recorder = GlobalScope.async {
                helper.executeTest(testData.timeoutTest.GherkinStyleTimeoutTests)
            }

            helper.assertExecutionEquals(
                    recorder.await().events()
            ) {
                group("GherkinStyleTimeoutTests") {
                    group("Feature: Timeouts") {
                        group("Scenario: Running more than 600ms") {
                            test("Then: It should fail", false)
                        }
                        group("Scenario: Running less than 1200ms") {
                            test("Then: It should succeed")
                        }
                    }
                }
            }
        }

        describe("global timeouts") {
            it("should use specified global timeout") {
                System.setProperty("spek2.execution.test.timeout", 100L.toString())
                val recorder = GlobalScope.async {
                    helper.executeTest(testData.timeoutTest.GlobalTimeoutTest)
                }

                helper.assertExecutionEquals(
                        recorder.await().events()
                ) {
                    group("GlobalTimeoutTest") {
                        test("this should run for 300ms and fail since global timeout is 100ms", false)
                    }
                }
                System.clearProperty("spek2.execution.test.timeout")
            }
        }
    }
})
