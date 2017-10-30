package org.spekframework.spek2.data_driven

import org.spekframework.spek2.Spek
import kotlin.test.assertEquals

class DataDrivenSpec : Spek({

    given("a calculator") {

        val calculator = Calculator()

        val data = arrayOf(
            data(4, 2, expected = 6),
            data(1, 3, expected = 4),
            data(5, 7, expected = 12)
        )

        on("addition %s and %s", with = *data) { input1, input2, expected ->

            it("returns $expected") {
                assertEquals(expected, calculator.add(input1, input2))
            }
        }

        on("%s subtract %s and subtract %s",
            data(10, 5, 3, expected = 2),
            data(100, 50, 8, expected = 42),
            data(0, 5, 3, expected = -8)
        ) { minuend, subtrahend1, subtrahend2, expected ->

            it("returns $expected") {
                assertEquals(calculator.subtract(calculator.subtract(minuend, subtrahend1), subtrahend2), expected)
            }
        }

        on("%s divided by %s", with = data(10, 2, expected = 5)) { dividend, divisor, result ->

            it("returns $result") {
                assertEquals(calculator.divide(dividend, divisor), result)
            }
        }
    }
})
