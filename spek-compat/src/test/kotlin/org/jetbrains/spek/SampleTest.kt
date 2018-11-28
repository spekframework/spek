package org.jetbrains.spek

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.on
import org.spekframework.spek2.Spek

object SampleTest: Spek({
    given("a list") {
        val list by memoized { mutableListOf<String>() }

        on("adding an item") {
            list.add("spek")

            it("should have a size of one") {
                assertThat(list.size, equalTo(1))
            }
        }
    }
})