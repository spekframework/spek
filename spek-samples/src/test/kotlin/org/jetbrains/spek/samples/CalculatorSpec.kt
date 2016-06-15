package org.jetbrains.spek.samples

import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

class CalculatorSpec: SubjectSpek<SampleCalculator>({
    subject { SampleCalculator() }

    it("should return the result of adding the first number to the second number") {
        val sum = subject.sum(2, 4)
        assertEquals(6, sum)
    }

    it("should return the result of subtracting the second number from the first number") {
        val subtract = subject.subtract(4, 2)
        assertEquals(2, subtract)
    }
})

