package testData.timeoutTest

import kotlinx.coroutines.channels.ReceiveChannel
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

class GherkinStyleTimeoutTests(latch: ReceiveChannel<Int>): Spek({
    Feature("Timeouts") {
        Scenario("Running more than 600ms") {
            defaultTimeout = 600
            Then("It should fail") {
                latch.receive()
            }
        }

        Scenario("Running less than default") {
            Then("It should succeed") {
                latch.receive()
            }
        }

        Scenario("Running less than 1200ms") {
            defaultTimeout = 1200
            Then("It should succeed") {
                latch.receive()
            }
        }
    }
})