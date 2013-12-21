package org.spek.api

import kotlin.test.*
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

public trait Spek: SkipSupport {
    fun given(description: String, givenExpression: Given.() -> Unit)
    fun given(description: String)
}

public trait Given: SkipSupport {
    fun on(description: String, onExpression: On.() -> Unit)
    fun on(description: String)
}

public trait On: SkipSupport {
    fun it(description: String, itExpression: It.() -> Unit)
    fun it(description: String)
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
        assertTrue(actual == false)
    }
}

trait SkipSupport {
    fun skip(why: String)
    fun skip()

    fun pending(why: String)
    fun pending()
}
