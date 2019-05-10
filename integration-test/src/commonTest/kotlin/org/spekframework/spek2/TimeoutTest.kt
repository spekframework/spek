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
})
