package org.jetbrains.spek.api

import kotlin.test.*

fun <T> It.shouldEqual(expected: T, actual: T): Unit {
    assertEquals(expected, actual)
}

fun <T> It.shouldNotEqual(expected: T, actual: T): Unit {
    assertFalse(null) { expected == actual }
}

fun <T> It.shouldBeNull(actual: T?): Unit {
    assertNull(actual)
}

fun <T> It.shouldNotBeNull(actual: T?): Unit {
    assertNotNull(actual as Any, "")
}

fun <T> It.shouldBeTrue(actual: T): Unit {
    assertTrue(actual == true)
}

fun <T> It.shouldBeFalse(actual: T): Unit {
    assertTrue(actual == false)
}

fun <T : Throwable> It.shouldThrow(exceptionClass: Class<T>, block: () -> Unit): T {
    return assertFailsWith(exceptionClass.kotlin, block)
}
