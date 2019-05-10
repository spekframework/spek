package testData.timeoutTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

object DefaultTimeoutTest: Spek({
    fun forever() {
        while (true) {}
    }

    test("should timeout") {
        forever()
    }

    describe("timeout specification style") {
        it("should timeout") {
            forever()
        }
    }

    Feature("timeout gherkin style") {
        Scenario("some scenario") {
            Then("should timeout") {
                forever()
            }
        }
    }
})
