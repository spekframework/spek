package org.spekframework.spek2.samples.subject

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import org.spekframework.spek2.samples.Calculator
import org.spekframework.spek2.subject.SubjectSpek
import kotlin.test.assertEquals

object CalculatorSpec: SubjectSpek<Calculator>({
    subject { Calculator() }

    describe("addition") {
        it("should return the result of adding the first number to the second number") {
            val sum = subject.add(2, 4)
            assertEquals(6, sum)
        }
    }

    describe("subtract") {
        it("should return the result of subtracting the second number from the first number") {
            val subtract = subject.subtract(4, 2)
            assertEquals(2, subtract)
        }
    }

    describe("division") {
        it("should return the result of dividing the first number by the second number") {
            assertEquals(2, subject.divide(4, 2))
        }

        context("division by zero") {
            it("should throw an exception") {
                assertThat({
                    subject.divide(2, 0)
                }, throws<IllegalArgumentException>())
            }
        }
    }
})

