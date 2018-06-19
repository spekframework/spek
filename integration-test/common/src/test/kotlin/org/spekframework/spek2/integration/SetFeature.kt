package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Given
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

object SetFeature: Spek({
    Given("Set") {
        val set by memoized { mutableSetOf<String>() }

        When("is empty") {
            Then("should have a size of 0") {
                assertEquals(0, set.size)
            }

            Then("should throw when first is invoked") {
                assertFailsWith(NoSuchElementException::class) {
                    set.first()
                }
            }
        }
    }
})
