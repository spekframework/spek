package org.spek.samples

import org.junit.Test as test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.spek.impl.AbstractSpek

class IncUtilJUnitSpecs: AbstractSpek() {{
    given("an inc util") {

        val incUtil = SampleIncUtil()

        on("calling incVaueBy with 4 and given number 6") {

            val result = incUtil.incValueBy(4, 6)

            it("should return 10") {

                shouldEqual(result, 10)

            }

            it("and it should not return 11") {

                shouldNotEqual(result, 11)

            }
        }

        on("calling incValueBy with 10 and given number 2") {
            val result = incUtil.incValueBy(10, 2)

            it("shut return 12") {
                assertEquals(result, 12)
            }
        }
    }
}
}

class X {
    test fun first() {
        assertTrue(true)
    }

    test fun second() {
        assertTrue(true)
    }
}



