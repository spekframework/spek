package org.jetbrains.spek.samples.subject

import org.jetbrains.samples.Calculator
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.table.testCase
import org.jetbrains.spek.table.unroll
import kotlin.test.assertEquals

object CalculatorTableSpec : Spek({
    describe("subtract") {
        unroll(
                testCase(4, 2),
                testCase(5, 3)
        ) { a, b ->
            it("should return the result of subtracting $b from $a") {
                val subtract = Calculator().subtract(a, b)
                assertEquals(2, subtract)
            }
        }
    }

})

