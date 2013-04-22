package org.spek.api

import kotlin.test.*
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

public trait Spek: SkipSupport<Spek> {

    fun given(description: String, givenExpression: Given.() -> Unit)
}

public trait Given: SkipSupport<Given> {

    fun on(description: String, onExpression: On.() -> Unit)
}

public trait On: SkipSupport<On> {

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

public trait SkipSupport<T> {
    fun skip(why: String = "not given"): T
}

Retention(RetentionPolicy.RUNTIME) public annotation class skip(val why: String)
