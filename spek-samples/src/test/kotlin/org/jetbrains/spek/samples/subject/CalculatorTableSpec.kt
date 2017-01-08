package org.jetbrains.spek.samples.subject

import org.jetbrains.samples.Calculator
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.table.example
import org.jetbrains.spek.table.with
import kotlin.test.assertEquals

object CalculatorTableSpec : Spek({
    describe("subtract") {
        with(
            example(4, 2),
            example(5, 3)
        ) { a, b ->
            it("should return the result of subtracting $b from $a") {
                val subtract = Calculator().subtract(a, b)
                assertEquals(2, subtract)
            }
        }
    }

})

