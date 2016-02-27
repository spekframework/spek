package org.jetbrains.spek.samples

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class SimpleTest : Spek({

    describe("a calculator") {
        val calculator = SampleCalculator()
        val sum = calculator.sum(2, 4)
        it("should return the result of adding the first number to the second number") {
            assertEquals(6, sum)
        }
        val subtract = calculator.subtract(4, 2)
        it("should return the result of subtracting the second number from the first number") {
            assertEquals(2, subtract)
        }
    }
})

