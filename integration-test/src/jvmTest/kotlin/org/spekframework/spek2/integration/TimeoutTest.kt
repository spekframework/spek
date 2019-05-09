package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.meta.Ignore
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

// remove @Ignore to test if timeouts are working.
@Ignore
object TimeoutTest: Spek({
    test("default timeout") {
        while (true) {}
    }

    test("custom timeout", timeout = 2000L) {
        while (true) {}
    }

    describe("specification default timeout") {
        defaultTimeout = 3000L
        it("should timeout") {
            while (true) {}
        }

        it("should custom timeout", timeout = 1000L) {
            while (true) {}
        }
    }

    Feature("feature-level default timeout") {
        defaultTimeout = 500L
        Scenario("bar") {
            Then("should timeout") {
                while (true) {}
            }
        }
    }

    Feature("scenario-level default timeout") {
        Scenario("bar") {
            defaultTimeout = 900L
            Then("should timeout") {
                while (true) {}
            }
        }
    }
})
