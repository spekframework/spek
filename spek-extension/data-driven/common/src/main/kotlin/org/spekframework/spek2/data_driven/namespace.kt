@file:Suppress("NOTHING_TO_INLINE")

package org.spekframework.spek2.data_driven

import org.spekframework.spek2.dsl.ActionBody
import org.spekframework.spek2.dsl.SpecBody

expect fun String.format(vararg args: Any?): String

inline fun <I1, Expected> SpecBody.on(description: String, vararg with: Data1<I1, Expected>, crossinline body: ActionBody.(I1, Expected) -> Unit) {
    with.forEach { (input, expected) ->
        on(description = description.format(input, expected)) {
            body(input, expected)
        }
    }
}

inline fun <I1, I2, Expected> SpecBody.on(description: String, vararg with: Data2<I1, I2, Expected>, crossinline body: ActionBody.(I1, I2, Expected) -> Unit) {
    with.forEach { (input1, input2, expected) ->
        on(description = description.format(input1, input2, expected)) {
            body(input1, input2, expected)
        }
    }
}

inline fun <I1, I2, I3, Expected> SpecBody.on(description: String, vararg with: Data3<I1, I2, I3, Expected>, crossinline body: ActionBody.(I1, I2, I3, Expected) -> Unit) {
    with.forEach { (input1, input2, input3, expected) ->
        on(description = description.format(input1, input2, input3, expected)) {
            body(input1, input2, input3, expected)
        }
    }
}

inline fun <I1, I2, I3, I4, Expected> SpecBody.on(description: String, vararg with: Data4<I1, I2, I3, I4, Expected>, crossinline body: ActionBody.(I1, I2, I3, I4, Expected) -> Unit) {
    with.forEach { (input1, input2, input3, input4, expected) ->
        on(description = description.format(input1, input2, input3, input4, expected)) {
            body(input1, input2, input3, input4, expected)
        }
    }
}

inline fun <I1, I2, I3, I4, I5, Expected> SpecBody.on(description: String, vararg with: Data5<I1, I2, I3, I4, I5, Expected>, crossinline body: ActionBody.(I1, I2, I3, I4, I5, Expected) -> Unit) {
    with.forEach { (input1, input2, input3, input4, input5, expected) ->
        on(description = description.format(input1, input2, input3, input4, input5, expected)) {
            body(input1, input2, input3, input4, input5, expected)
        }
    }
}

inline fun <I1, I2, I3, I4, I5, I6, Expected> SpecBody.on(description: String, vararg with: Data6<I1, I2, I3, I4, I5, I6, Expected>, crossinline body: ActionBody.(I1, I2, I3, I4, I5, I6, Expected) -> Unit) {
    with.forEach { (input1, input2, input3, input4, input5, input6, expected) ->
        on(description = description.format(input1, input2, input3, input4, input5, input6, expected)) {
            body(input1, input2, input3, input4, input5, input6, expected)
        }
    }
}

inline fun <I1, I2, I3, I4, I5, I6, I7, Expected> SpecBody.on(description: String, vararg with: Data7<I1, I2, I3, I4, I5, I6, I7, Expected>, crossinline body: ActionBody.(I1, I2, I3, I4, I5, I6, I7, Expected) -> Unit) {
    with.forEach { (input1, input2, input3, input4, input5, input6, input7, expected) ->
        on(description = description.format(input1, input2, input3, input4, input5, input6, input7, expected)) {
            body(input1, input2, input3, input4, input5, input6, input7, expected)
        }
    }
}

inline fun <I1, I2, I3, I4, I5, I6, I7, I8, Expected> SpecBody.on(description: String, vararg with: Data8<I1, I2, I3, I4, I5, I6, I7, I8, Expected>, crossinline body: ActionBody.(I1, I2, I3, I4, I5, I6, I7, I8, Expected) -> Unit) {
    with.forEach { (input1, input2, input3, input4, input5, input6, input7, input8, expected) ->
        on(description = description.format(input1, input2, input3, input4, input5, input6, input7, input8, expected)) {
            body(input1, input2, input3, input4, input5, input6, input7, input8, expected)
        }
    }
}

inline fun <I1, I2, I3, I4, I5, I6, I7, I8, I9, Expected> SpecBody.on(description: String, vararg with: Data9<I1, I2, I3, I4, I5, I6, I7, I8, I9, Expected>, crossinline body: ActionBody.(I1, I2, I3, I4, I5, I6, I7, I8, I9, Expected) -> Unit) {
    with.forEach { (input1, input2, input3, input4, input5, input6, input7, input8, input9, expected) ->
        on(description = description.format(input1, input2, input3, input4, input5, input6, input7, input8, input9, expected)) {
            body(input1, input2, input3, input4, input5, input6, input7, input8, input9, expected)
        }
    }
}

data class Data1<out I1, out Expected>(val input1: I1, val expected: Expected)
data class Data2<out I1, out I2, out Expected>(val input1: I1, val input2: I2, val expected: Expected)
data class Data3<out I1, out I2, out I3, out Expected>(val input1: I1, val input2: I2, val input3: I3, val expected: Expected)
data class Data4<out I1, out I2, out I3, out I4, out Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: I4, val expected: Expected)
data class Data5<out I1, out I2, out I3, out I4, out I5, out Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: I4, val input5: I5, val expected: Expected)
data class Data6<out I1, out I2, out I3, out I4, out I5, out I6, out Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: I4, val input5: I5, val input6: I6, val expected: Expected)
data class Data7<out I1, out I2, out I3, out I4, out I5, out I6, out I7, out Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: I4, val input5: I5, val input6: I6, val input7: I7, val expected: Expected)
data class Data8<out I1, out I2, out I3, out I4, out I5, out I6, out I7, out I8, out Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: I4, val input5: I5, val input6: I6, val input7: I7, val input9: I8, val expected: Expected)
data class Data9<out I1, out I2, out I3, out I4, out I5, out I6, out I7, out I8, out I9, out Expected>(val input1: I1, val input2: I2, val input3: I3, val input4: I4, val input5: I5, val input6: I6, val input7: I7, val input8: I8, val input9: I9, val expected: Expected)

inline fun <I1, E> data(i1: I1, expected: E) = Data1(i1, expected)
inline fun <I1, I2, E> data(i1: I1, i2: I2, expected: E) = Data2(i1, i2, expected)
inline fun <I1, I2, I3, E> data(i1: I1, i2: I2, i3: I3, expected: E) = Data3(i1, i2, i3, expected)
inline fun <I1, I2, I3, I4, E> data(i1: I1, i2: I2, i3: I3, i4: I4, expected: E) = Data4(i1, i2, i3, i4, expected)
inline fun <I1, I2, I3, I4, I5, E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, expected: E) = Data5(i1, i2, i3, i4, i5, expected)
inline fun <I1, I2, I3, I4, I5, I6, E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, expected: E) = Data6(i1, i2, i3, i4, i5, i6, expected)
inline fun <I1, I2, I3, I4, I5, I6, I7, E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, expected: E) = Data7(i1, i2, i3, i4, i5, i6, i7, expected)
inline fun <I1, I2, I3, I4, I5, I6, I7, I8, E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, expected: E) = Data8(i1, i2, i3, i4, i5, i6, i7, i8, expected)
inline fun <I1, I2, I3, I4, I5, I6, I7, I8, I9, E> data(i1: I1, i2: I2, i3: I3, i4: I4, i5: I5, i6: I6, i7: I7, i8: I8, i9: I9, expected: E) = Data9(i1, i2, i3, i4, i5, i6, i7, i8, i9, expected)
