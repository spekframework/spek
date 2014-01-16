package org.spek.console.test.samples2

import org.spek.api.*
import org.spek.api.annotations.*
import org.spek.console.api.*


spek fun Spek.this_is_test() {
    given("Outside it is raining") {
        on("umbrella") {
            it("should not be raining") {
                shouldBeTrue(true)
            }
            it("should not be sunny") {
                shouldBeTrue(false)
            }
        }
    }
}
