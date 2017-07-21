package org.jetbrains.spek.data_driven

/**
 * @author Niels Falk
 */
class Where<I1, I2, Expected>(function: Where<I1, I2, Expected>.() -> Unit) {
    val data2 = mutableListOf<Data2<I1, I2, Expected>>()

    operator fun <I1> I1.not() = Row(this).apply {

    }

    data class Row<I1>(val i1: I1) {
        operator fun <I2> div(i2: I2) = Data1(i1, i2)
    }

    operator fun Data1<I1, I2>.div(newExpected: Expected) =
            Data2(input1, expected, newExpected)
                    .apply { data2.add(this) }
}

fun <I1, I2, Expected> where(function: Where<I1, I2, Expected>.() -> Unit): List<Data2<I1, I2, Expected>> {
    return Where(function)
            .apply { function() }
            .data2.toList()
}
