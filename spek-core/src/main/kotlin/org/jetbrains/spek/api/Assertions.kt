package org.jetbrains.spek.api

import kotlin.test.*

public fun It.shouldEqual<T>(expected: T, actual: T) : Unit {
    assertEquals(expected, actual)
}

public fun It.shouldNotEqual<T>(expected: T, actual: T) : Unit {
    assertNot { expected == actual }
}

public fun It.shouldBeNull<T>(actual: T) : Unit {
    assertNull(actual)
}

public fun It.shouldNotBeNull<T>(actual: T) : Unit {
    assertNotNull(actual)
}

public fun It.shouldBeTrue<T>(actual: T) : Unit {
    assertTrue(actual == true)
}

public fun It.shouldBeFalse<T>(actual: T) : Unit {
    assertTrue(actual == false)
}
