package org.jetbrains.spek.samples

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class SimpleTest : Spek({
    describe("a calculator") {
        val calculator = SampleCalculator()

        it("should return the result of adding the first number to the second number") {
            val sum = calculator.sum(2, 4)
            assertEquals(6, sum)
        }

        it("should return the result of subtracting the second number from the first number") {
            val subtract = calculator.subtract(4, 2)
            assertEquals(2, subtract)
        }
    }
})

