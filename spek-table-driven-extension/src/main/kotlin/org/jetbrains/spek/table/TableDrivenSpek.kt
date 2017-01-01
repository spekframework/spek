package org.jetbrains.spek.table

import org.jetbrains.spek.api.dsl.TestContainer

interface TableDrivenSpek

fun <A> testCase(i: A): TestCase<A> = TestCase(i)
fun <A, B> testCase(i: A, s: B): TestCase2<A, B> = TestCase2(i, s)

class TestCase<out T> internal constructor(val value: T)
class TestCase2<out A, out B> internal constructor(val value: A, val value2: B)

fun <T> TestContainer.unroll(vararg testCases: TestCase<T>, function: TestContainer.(T) -> Unit) {
    testCases.forEach { function.invoke(this, it.value) }
}

fun <A, B> TestContainer.unroll(vararg testCases: TestCase2<A, B>, function: TestContainer.(A, B) -> Unit) {
    testCases.forEach { function.invoke(this, it.value, it.value2) }
}
