package org.spek.samples

import org.junit.Test as test
import org.spek.junit.api.JUnitSpek
import kotlin.test.assertEquals

class IncUtilJUnitSpecs: JUnitSpek() {{
    given("an inc util") {

        val incUtil = SampleIncUtil()

        on("calling incVaueBy with 4 and given number 6") {

            val result = incUtil.incValueBy(4, 6)

            it("should return 10") {

                shouldEqual(10, result)

            }
        }

        on("calling incValueBy with 10 and given number 2") {
            val result = incUtil.incValueBy(10, 2)

            it("shut return 12") {
                assertEquals(12, result)
            }
        }
    }
}}




