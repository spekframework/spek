package org.spek.samples

import org.spek.junit.*
import org.spek.*

class IncUtilJUnitSpecs: Spek() {{
    given("an inc util") {

        val incUtil = SampleIncUtil()

        on("calling incValueBy with 4 and given number 6") {

            val result = incUtil.incValueBy(4, 6)

            it("should return 10") {

                shouldEqual(10, result)

            }
        }

        on("calling incValueBy with 10 and given number 2") {
            val result = incUtil.incValueBy(10, 2)

            it("should return 12") {
                shouldNotEqual(12, result)
            }
        }
    }
}}




