package org.spek.samples

import org.spek.junit.*
import org.spek.*

class CalculatorJUnitSpecs: Spek() {{
    given("a calculator") {
        val calculator = SampleCalculator()
        on("calling sum with two numbers") {
            val sum = calculator.sum(2, 4)

            it("should return the result of adding the first number to the second number") {
                shouldEqual(6, sum)
            }

            it("should still be same value") {
                shouldEqual(6, sum)
            }
        }

        on("calling substract with two numbers") {
            val subtract = calculator.subtract(4, 2)

            it("should return the result of substracting the second number from the first number") {
                shouldEqual(2, subtract)
            }
        }
    }
}}

