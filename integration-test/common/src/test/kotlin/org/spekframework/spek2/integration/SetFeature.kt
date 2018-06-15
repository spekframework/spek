package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

object SetFeature: Spek({

    Feature("Add Items to List") {

        val list by memoized { mutableListOf<String>() }

        Scenario("Adding item") {

            When("add item") {
                list.add("item")
            }

            Then("Size should be 1") {
                assertEquals(1, list.size)
            }
        }
    }
})
