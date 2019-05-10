package org.spekframework.spek2

import org.spekframework.spek2.style.specification.describe

object FailureTest: AbstractSpekTest({ helper ->
    describe("failed tests") {
        it("should be reported properly") {
            val recorder = helper.executeTest(testData.failureTest.FailingTest)

            helper.assertExecutionEquals(recorder.events()) {
                group("FailingTest") {
                    group("equality") {
                        test("4 == 2 + 2")
                        test("false == true", false)
                    }
                }
            }
        }
    }
})
