package org.jetbrains.spek.table

import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.api.dsl.TestContainer

fun <A> testCase(a: A): TestCase<A> = TestCase(a)
fun <A, B> testCase(a: A, b: B): TestCase2<A, B> = TestCase2(a, b)
fun <A, B, C> testCase(a: A, b: B, c: C): TestCase3<A, B, C> = TestCase3(a, b, c)

class TestCase<out A> internal constructor(val a: A)
class TestCase2<out A, out B> internal constructor(val a: A, val b: B)
class TestCase3<out A, out B, out C> internal constructor(val a: A, val b: B, val c: C)

fun <T> Spec.unroll(vararg testCases: TestCase<T>, testContent: TestContainer.(T) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.a) }
}

fun <A, B> Spec.unroll(vararg testCases: TestCase2<A, B>, testContent: TestContainer.(A, B) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.a, it.b) }
}

fun <A, B, C> Spec.unroll(vararg testCases: TestCase3<A, B, C>, testContent: TestContainer.(A, B, C) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.a, it.b, it.c) }
}
