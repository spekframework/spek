package org.jetbrains.spek.samples
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class ContextGivenOnTest : Spek({
    given("a calculator") {
        println("given")
        val calculator = SampleCalculator()
        var result = 0

        context("addition") {
            println("context")
            beforeEach {
                println("before")
                result = calculator.sum(2, 4);
            }

            it("should return the result of adding the first number to the second number") {
                println("it1")
                assertEquals(6, result)
            }
            it("should fail") {
                println("it2")
                assertEquals(7, result)
            }
        }

        on("subtraction") {
            println("on")
            beforeEach {
                println("before2")
                result = calculator.subtract(4, 2)
            }

            it("should return the result of subtracting the second number from the first number") {
                println("it3")
                assertEquals(2, result)
            }
        }
    }
})

