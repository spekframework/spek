package org.jetbrains.spek.samples

import org.jetbrains.spek.Spek
import org.jetbrains.spek.dsl.describe
import org.jetbrains.spek.dsl.it
import kotlin.test.assertEquals

class CalculatorSpec: Spek({
    describe(SampleCalculator::class) {
        subject { SampleCalculator() }

        it("should return the result of adding the first number to the second number") {
            val sum = subject.sum(2, 4)
            assertEquals(6, sum)
        }

        it("should return the result of subtracting the second number from the first number") {
            val subtract = subject.subtract(4, 2)
            assertEquals(2, subtract)
        }
    }
})

