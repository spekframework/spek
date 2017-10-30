package org.spekframework.spek2.data_driven

import org.spekframework.spek2.dsl.ActionBody
import org.spekframework.spek2.dsl.SpecBody

expect fun String.format(vararg args: Any?): String

inline fun <reified I1, reified Expected> SpecBody.on(description: String, vararg with: Data1<I1, Expected>, crossinline body: ActionBody.(i1: I1, e: Expected) -> Unit) {
    with.forEach {
        val (input, expected) = it
        on(description = description.format(input, expected)) {
            body(this, input, expected)
        }
    }
}

inline fun <reified I1, reified I2, reified Expected> SpecBody.on(description: String, vararg with: Data2<I1, I2, Expected>, crossinline body: ActionBody.(i1: I1, i2: I2, e: Expected) -> Unit) {
    with.forEach {
        val (input1, input2, expected) = it
        on(description = description.format(input1, input2, expected)) {
            body(this, input1, input2, expected)
        }
    }
}

inline fun <reified I1, reified I2, reified I3, reified Expected> SpecBody.on(description: String, vararg with: Data3<I1, I2, I3, Expected>, crossinline body: ActionBody.(i1: I1, i2: I2, i3: I3, e: Expected) -> Unit) {
    with.forEach {
        val (input1, input2, input3, expected) = it
        on(description = description.format(input1, input2, input3, expected)) {
            body(this, input1, input2, input3, expected)
        }
    }
}

data class Data1<I1, Expected>(val input1: I1, val expected: Expected)
data class Data2<I1, I2, Expected>(val input1: I1, val input2: I2, val expected: Expected)
data class Data3<I1, I2, I3, Expected>(val input1: I1, val input2: I2, val input3: I3, val expected: Expected)
data class Data4<I1, I2, I3, T4, Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: T4, val expected: Expected)
data class Data5<I1, I2, I3, T4, T5, Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: T4, val input5: T5, val expected: Expected)
data class Data6<I1, I2, I3, T4, T5, T6, Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: T4, val input5: T5, val input6: T6, val expected: Expected)
data class Data7<I1, I2, I3, T4, T5, T6, T7, Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: T4, val input5: T5, val input6: T6, val input7: T7, val expected: Expected)
data class Data8<I1, I2, I3, T4, T5, T6, T7, T8, Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: T4, val input5: T5, val input6: T6, val input7: T7, val input9: T8, val expected: Expected)
data class Data9<I1, I2, I3, T4, T5, T6, T7, T8, T9, Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: T4, val input5: T5, val input6: T6, val input7: T7, val input8: T8, val input9: T9, val expected: Expected)

inline fun <reified I1, reified E> data(i1: I1, expected: E) = Data1(i1, expected = expected)
inline fun <reified I1, reified I2, reified E> data(i1: I1, i2: I2, expected: E) = Data2(i1, i2, expected = expected)
inline fun <reified I1, reified I2, reified I3, reified E> data(i1: I1, i2: I2, i3: I3, expected: E) = Data3(i1, i2, i3, expected = expected)
inline fun <reified I1, reified I2, reified I3, reified I4, reified E> data(i1: I1, i2: I2, i3: I3, i4: I4, expected: E) = Data4(i1, i2, i3, i4, expected = expected)
inline fun <reified I1, reified I2, reified I3, reified I4, reified I5, reified E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, expected: E) = Data5(i1, i2, i3, i4, i5, expected = expected)
inline fun <reified I1, reified I2, reified I3, reified I4, reified I5, reified I6, reified E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, expected: E) = Data6(i1, i2, i3, i4, i5, i6, expected = expected)
inline fun <reified I1, reified I2, reified I3, reified I4, reified I5, reified I6, reified I7, reified E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, expected: E) = Data7(i1, i2, i3, i4, i5, i6, i7, expected = expected)
inline fun <reified I1, reified I2, reified I3, reified I4, reified I5, reified I6, reified I7, reified I8, reified E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, expected: E) = Data8(i1, i2, i3, i4, i5, i6, i7, i8, expected = expected)
inline fun <reified I1, reified I2, reified I3, reified I4, reified I5, reified I6, reified I7, reified I8, reified I9, reified E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9, expected: E) = Data9(i1, i2, i3, i4, i5, i6, i7, i8, i9, expected = expected)
