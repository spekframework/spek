package testData.timeoutTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

object CustomTimeoutTest: Spek({
    test("should timeout", timeout = 500) {
        sleep(2000)
    }

    describe("timeout specification style") {
        it("should timeout", timeout = 100) {
            sleep(2000)
        }
    }

    Feature("timeout gherkin style") {
        Scenario("some scenario") {
            defaultTimeout = 1000
            Then("should timeout") {
                sleep(3000)
            }
        }
    }
})
