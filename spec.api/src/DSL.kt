package org.spek.api

import kotlin.test.*
import org.spek.impl.*
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Retention

public fun given(description: String, givenExpression: Given.() -> Unit): TestGivenAction
        = givenImpl(description, givenExpression)

public trait Given {
    fun on(description: String, onExpression: On.() -> Unit)
}

public trait On {
    fun it(description: String, itExpression: It.()->Unit)
}

public class It {

    fun shouldEqual<T>(expected: T, actual: T) {
        assertEquals(expected, actual)
    }

    fun shouldNotEqual<T>(expected: T, actual: T) {
        assertNot { expected == actual }
    }

    fun shouldBeNull<T>(actual: T) {
        assertNull(actual)
    }

    fun shouldNotBeNull<T>(actual: T) {
        assertNotNull(actual)
    }

    fun shouldBeTrue<T>(actual: T) {
        assertTrue(actual == true)
    }

    fun shouldBeFalse<T>(actual: T) {
        assertFalse(actual == false)
    }
}
