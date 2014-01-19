package org.spek.samples

import org.spek.console.api.ConsoleSpek
import org.spek.api.*

class IncUtilConsoleSpecs: ConsoleSpek() {{
    given("an inc util") {

        val incUtil = SampleIncUtil()

        on("calling incValueBy with 4 and given number 6") {

            val result = incUtil.incValueBy(4, 6)

            it("should return 10") {

                shouldEqual(result, 10)

            }
        }

        on("calling incValueBy with 10 and given number 2") {
            val result = incUtil.incValueBy(10, 2)

            it("shut return 12") {
                shouldNotEqual(result, 10)
            }
        }
    }
}}


