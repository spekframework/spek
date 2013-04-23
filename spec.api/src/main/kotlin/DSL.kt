package org.spek.api

import kotlin.test.*
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

public trait Spek: SkipSupport {

    fun given(description: String, givenExpression: Given.() -> Unit = { throw PendingException("not given") })
}

public trait Given {

    fun on(description: String, onExpression: On.() -> Unit = { throw PendingException("not given") })
}

public trait On {

    fun it(description: String, itExpression: It.()->Unit = { throw PendingException("not given") })
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

trait SkipSupport {
    final fun skip(why: String = "not given") = throw SkippedException(why)
    final fun pending(why: String = "not given") = throw PendingException(why)
}

/*
*  TODO: the parameter (why) could be optional but due to this bug (#KT-3197),
*        I had no choice but to make it mandatory for now.
*  TODO: need to be refactored when #KT-3197 got fixed.
*/
Retention(RetentionPolicy.RUNTIME) public annotation class skip(val why: String)

class SkippedException(message: String): RuntimeException(message)

class PendingException(message: String): RuntimeException(message)