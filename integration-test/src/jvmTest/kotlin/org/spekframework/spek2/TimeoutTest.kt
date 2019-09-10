package org.spekframework.spek2

import org.spekframework.spek2.style.specification.describe

object TimeoutTest: AbstractSpekTest({ helper ->
    describe("test timeouts") {
        // FIXME: provide a way to disable timeouts.
        it("should timeout using default settings", timeout = 60000L) {
            val recorder = helper.executeTest(testData.timeoutTest.DefaultTimeoutTest)

            helper.assertExecutionEquals(
                recorder.events()
            ) {
                group("DefaultTimeoutTest") {
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
        it("should use specified global timeout", timeout = 30000) {
            System.setProperty("SPEK_TIMEOUT", 20000L.toString())
            val recorder = helper.executeTest(testData.timeoutTest.GlobalTimeoutTest)

            helper.assertExecutionEquals(
                recorder.events()
            ) {
                group("GlobalTimeoutTest") {
                    test("this should run for 15 seconds and pass since global timeout is 20")
                }
            }
            System.clearProperty("SPEK_TIMEOUT")
        }
    }
})
