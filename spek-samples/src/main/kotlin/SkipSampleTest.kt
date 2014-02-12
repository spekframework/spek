package org.spek.samples

import org.spek.console.*
import org.spek.*

class SkipSample: Spek() {{
    given("a sample") {
        on("calling a functiono") {
            val result = 10
            it("should return 10") {
                shouldEqual(result, 10)
            }
            skip("skip")
            it("should not return 11") {
                shouldNotEqual(result, 11)
            }
        }
    }
}}

