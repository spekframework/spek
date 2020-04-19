package testData.timeoutTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

object DefaultTimeoutTest: Spek({
    test("should not timeout") {
        sleep(13000)
    }

    describe("timeout specification style") {
        it("should not timeout") {
            sleep(13000)
        }
    }

    Feature("timeout gherkin style") {
        Scenario("some scenario") {
            Then("should not timeout") {
                sleep(13000)
            }
        }
    }
})
