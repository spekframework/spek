package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

object SetFeature: Spek({
    Feature("Set") {
        val set by memoized { mutableSetOf<String>() }

        Scenario("is empty") {
            Then("should have a size of 0") {
                assertEquals(0, set.size)
            }

            And("should throw when first is invoked") {
                assertFailsWith(NoSuchElementException::class) {
                    set.first()
                }
            }
        }
    }
})
