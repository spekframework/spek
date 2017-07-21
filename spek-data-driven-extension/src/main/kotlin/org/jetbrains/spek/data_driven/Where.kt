package org.jetbrains.spek.data_driven

/**
 * @author Niels Falk
 */
class Where<I1, I2, I3>(function: Where<I1, I2, I3>.() -> Unit) {
    val data1 = mutableListOf<Data1<I1, I2>>()
    val data2 = mutableListOf<Data2<I1, I2, I3>>()
    private val headerCell = ""

    infix fun String.I(i: String) = headerCell

    infix fun String.II(i: String) = headerCell

    infix fun I1.I(i: I2): Data1<I1, I2> = separateCell(i)

    infix fun I1.II(i: I2): Data1<I1, I2> = separateCell(i)

    private fun I1.separateCell(i: I2) =
            Data1(this, i)
                    .apply { data1.add(this) }


    infix fun Data1<I1, I2>.I(i3: I3) =
            separateCell(i3)

    infix fun Data1<I1, I2>.II(i3: I3) = separateCell(i3)

    private fun Data1<I1, I2>.separateCell(i3: I3) = Data2(input1, expected, i3)
            .apply { data2.add(this) }
}

fun <I1, I2, I3> where(function: Where<I1, I2, I3>.() -> Unit): List<Data2<I1, I2, I3>> {
    val result = Where(function).apply {
        function()
    }.data2
    return result.toList()
}
