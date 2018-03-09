package org.spekframework.spek2.samples

import org.spekframework.spek2.Spek
import org.spekframework.spek2.data_driven.data
import org.spekframework.spek2.data_driven.on
import kotlin.test.assertEquals

class DataDrivenSpec : Spek({
    val calculator by memoized { Calculator() }

    context("sum") {
        val data = arrayOf(
                data(2, 2, 4),
                data(1, 9, 10),
                data(5, 5, 10),
                data(4, 96, 100)
        )

        on("%d plus %d should be a %d", *data) { first, second, result ->
            assertEquals(result, calculator.add(first, second))
        }
    }

    context("divide") {
        val data = arrayOf(
                data(4, 2, 2),
                data(9, 3, 3),
                data(24, 4, 6)
        )

        on("%d divided by %d should be a %d", *data) { first, second, result ->
            assertEquals(result, calculator.divide(first, second))
        }
    }
})
