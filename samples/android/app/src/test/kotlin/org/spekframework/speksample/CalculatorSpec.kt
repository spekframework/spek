package org.spekframework.speksample

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object CalculatorSpec : Spek({
    describe("A Calculator") {
        val calculator by memoized { Calculator() }

        it("should return 4") {
            assertEquals(expected = 4, actual = calculator.add(2, 2))
        }

        it("should return 2") {
            assertEquals(expected = 2, actual = calculator.subtract(4, 2))
        }

        it("should return 4") {
            assertEquals(expected = 4, actual = calculator.mul(2, 2))
        }

        it("should return 2") {
            assertEquals(expected = 2, actual = calculator.div(4, 2))
        }
    }
})
