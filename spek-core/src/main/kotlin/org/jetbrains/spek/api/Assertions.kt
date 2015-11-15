package org.jetbrains.spek.api

import kotlin.test.*

public fun <T> It.shouldEqual(expected: T, actual: T) : Unit {
    assertEquals(expected, actual)
}

public fun <T> It.shouldNotEqual(expected: T, actual: T) : Unit {
    assertFalse(null) { expected == actual }
}

public fun <T> It.shouldBeNull(actual: T?) : Unit {
    assertNull(actual)
}

public fun <T> It.shouldNotBeNull(actual: T?) : Unit {
    assertNotNull(actual as Any, "")
}

public fun <T> It.shouldBeTrue(actual: T) : Unit {
    assertTrue(actual == true)
}

public fun <T> It.shouldBeFalse(actual: T) : Unit {
    assertTrue(actual == false)
}

public fun <T: Throwable> It.shouldThrow(exceptionClass: Class<T>, block: () -> Unit): T {
    return assertFailsWith(exceptionClass, block)
}



