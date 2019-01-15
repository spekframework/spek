package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite
import org.spekframework.spek2.style.specification.describe
import kotlin.math.pow
import kotlin.test.assertEquals

open class Calculator {
    fun add(x: Int, y: Int) = x + y
}

class AdvancedCalculator: Calculator() {
    fun pow(x: Int, y: Int) = x.toDouble().pow(y).toInt()
}


object CalculatorSpecs: Spek({
    fun Suite.behavesLikeACalculator() {
        val calculator by memoized<Calculator>()

        it("1 + 2 = 3") {
            assertEquals(3, calculator.add(1, 2))
        }
    }

    describe("Calculator") {
        val calculator by memoized { Calculator() }

        behavesLikeACalculator()
    }

    describe("AdvancedCalculator") {
        val calculator by memoized { AdvancedCalculator() }

        behavesLikeACalculator()

        it("2 ^ 3 = 8") {
            assertEquals(8, calculator.pow(2, 3))
        }
    }
})
