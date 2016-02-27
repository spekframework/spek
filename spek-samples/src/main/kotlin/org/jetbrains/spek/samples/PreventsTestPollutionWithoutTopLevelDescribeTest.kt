package org.jetbrains.spek.samples

import org.jetbrains.spek.api.*
import kotlin.test.assertEquals

class PreventsTestPollutionWithoutTopLevelDescribeTest : Spek({
    var number = 3

    it("should equal 3") {
        assertEquals(number, 3)
        number = 4
    }

    it("should still equal 3") {
        assertEquals(number, 3)
    }

    describe("nested describe") {
        var anotherNumber = 5
        number = 9

        it("works for nested describes") {
            assertEquals(anotherNumber, 5)
            assertEquals(number, 9)
            number = 8
            anotherNumber = 10
        }

        it("works for nested describes again") {
            assertEquals(anotherNumber, 5)
            assertEquals(number, 9)
            number = 8
            anotherNumber = 10
        }
    }

    it("should still equal 3") {
        assertEquals(number, 3)
    }
})




