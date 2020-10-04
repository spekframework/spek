package testData.timeoutTest

import kotlinx.coroutines.delay
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object GherkinStyleTimeoutTests: Spek({
    Feature("Timeouts") {
        Scenario("Running more than 600ms") {
            defaultTimeout = 600
            Then("It should fail") {
                delay(800)
            }
        }

        Scenario("Running less than 1200ms") {
            defaultTimeout = 1200
            Then("It should succeed") {
                delay(100)
            }
        }
    }
})