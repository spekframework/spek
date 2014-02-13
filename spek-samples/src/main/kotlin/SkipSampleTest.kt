package org.spek.samples

import org.spek.console.*
import org.spek.*

class SkipSample: Spek() {{
    given("a sample") {
        on("calling a function") {
            val result = 10
            it("should return 10") {
                skip("Obsolete")
                shouldEqual(result, 10)
            }
            it("should not return 11") {
                pending("waiting for John to implement some functionality")
            }
        }
    }
}}

