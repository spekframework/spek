package org.jetbrains.spek.data_driven

import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.ActionBody
import org.jetbrains.spek.api.dsl.on as defaultOn


inline fun <reified I1, reified Expected> SpecBody.on(description: String, vararg with: Data1<I1, Expected>, crossinline body: ActionBody.(i1: I1, e: Expected) -> Unit) {
    with.forEach {
        val (input, expected) = it
        defaultOn(description = description.format(input, expected)) {
            body(this, input, expected)
        }
    }
}

inline fun <reified I1, reified I2, reified Expected> SpecBody.on(description: String, vararg with: Data2<I1, I2, Expected>, crossinline body: ActionBody.(i1: I1, i2: I2, e: Expected) -> Unit) {
    with.forEach {
        val (input1, input2, expected) = it
        defaultOn(description = description.format(input1, input2, expected)) {
            body(this, input1, input2, expected)
        }
    }
}

inline fun <reified I1, reified I2, reified I3, reified Expected> SpecBody.on(description: String, vararg with: Data3<I1, I2, I3, Expected>, crossinline body: ActionBody.(i1: I1, i2: I2, i3: I3, e: Expected) -> Unit) {
    with.forEach {
        val (input1, input2, input3, expected) = it
        defaultOn(description = description.format(input1, input2, input3, expected)) {
            body(this, input1, input2, input3, expected)
        }
    }
}

data class Data1<I1, Expected>(val input1: I1, val expected: Expected)
data class Data2<I1, I2, Expected>(val input1: I1, val input2: I2, val expected: Expected)
data class Data3<I1, I2, I3, Expected>(val input1: I1, val input2: I2, val input3: I3, val expected: Expected)

inline fun <reified I1, reified E> data(i1: I1, expected: E) = Data1(i1, expected = expected)
inline fun <reified I1, reified I2, reified E> data(i1: I1, i2: I2, expected: E) = Data2(i1, i2, expected = expected)
inline fun <reified I1, reified I2, reified I3, reified E> data(i1: I1, i2: I2, i3: I3, expected: E) = Data3(i1, i2, i3, expected = expected)
