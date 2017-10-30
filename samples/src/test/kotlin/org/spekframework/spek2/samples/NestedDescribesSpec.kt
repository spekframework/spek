package org.spekframework.spek2.samples

import org.spekframework.spek2.Spek
import kotlin.test.assertEquals

class NestedDescribesSpec : Spek({
    describe("a calculator") {
        val calculator = Calculator()
        var result = 0

        describe("addition") {
            beforeEachTest {
                result = calculator.add(2, 4);
            }

            it("should return the result of adding the first number to the second number") {
                assertEquals(6, result)
            }
            it("should fail") {
                assertEquals(6, result)
            }
        }

        describe("subtraction") {
            beforeEachTest {
                result = calculator.subtract(4, 2)
            }

            it("should return the result of subtracting the second number from the first number") {
                assertEquals(2, result)
            }
        }
    }
})

