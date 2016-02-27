package org.jetbrains.spek.samples


import kotlin.test.assertEquals

class NestedDescribesTest : org.jetbrains.spek.api.Spek({
    describe("a calculator") {
        val calculator = SampleCalculator()
        describe("addition") {
            val sum = calculator.sum(2, 4)
            it("should return the result of adding the first number to the second number") {
                assertEquals(6, sum)
            }
            it("should fail") {
                assertEquals(7, sum)
            }
        }
        describe("subtraction") {
            val subtract = calculator.subtract(4, 2)

            it("should return the result of subtracting the second number from the first number") {
                assertEquals(2, subtract)
            }
        }
    }
})

