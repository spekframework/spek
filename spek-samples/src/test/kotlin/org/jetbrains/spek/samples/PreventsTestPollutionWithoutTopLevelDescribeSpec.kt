package org.jetbrains.spek.samples

import org.jetbrains.spek.Spek
import org.jetbrains.spek.dsl.describe
import org.jetbrains.spek.dsl.it
import kotlin.test.assertEquals

class PreventsTestPollutionWithoutTopLevelDescribeSpec: Spek({
    var outerNumber1 = 0
    var outerNumber2 = 0

    beforeEach {
        outerNumber1 = 3
        outerNumber2 = 100
    }

    it("should equal 3") {

        assertEquals(outerNumber1, 3)
        assertEquals(outerNumber2, 100)
        outerNumber1 = 4
    }

    it("should still equal 3") {
        assertEquals(outerNumber1, 3)
        assertEquals(outerNumber2, 100)
    }

    describe("nested describe") {
        var innerNumber = 0

        beforeEach {
            innerNumber = 5
            outerNumber1 = 9
        }

        it("works for nested describes") {
            assertEquals(innerNumber, 5)
            assertEquals(outerNumber1, 9)
            assertEquals(outerNumber2, 100)

            outerNumber1 = 8
            innerNumber = 10
            outerNumber2 = 99
        }

        it("works for nested describes again") {
            assertEquals(innerNumber, 5)
            assertEquals(outerNumber1, 9)
            assertEquals(outerNumber2, 100)

            outerNumber1 = 8
            innerNumber = 10
        }
    }

    it("should still equal 3") {
        assertEquals(outerNumber1, 3)
    }
})




