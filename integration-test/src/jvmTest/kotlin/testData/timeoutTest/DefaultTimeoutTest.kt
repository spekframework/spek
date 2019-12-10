package testData.timeoutTest

import kotlinx.coroutines.channels.ReceiveChannel
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

class DefaultTimeoutTest(latch: ReceiveChannel<Int>): Spek({
    test("should timeout") {
        latch.receive()
    }

    test("should not timeout") {
        println(latch.receive())
    }

    describe("timeout specification style") {
        it("should timeout") {
            latch.receive()
        }

        it("should not timeout") {
            println(latch.receive())
        }
    }

    Feature("timeout gherkin style") {
        Scenario("timeout scenario") {
            Then("should timeout") {
                latch.receive()
            }
        }

        Scenario("not timeout scenario") {
            Then("should not timeout") {
                latch.receive()
            }
        }
    }
})
