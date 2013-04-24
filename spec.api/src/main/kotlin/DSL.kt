package org.spek.api

import kotlin.test.*
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

public trait Spek: SkipSupport {

    fun given(description: String, givenExpression: Given.() -> Unit = { pending() })
}

public trait Given: SkipSupport {

    fun on(description: String, onExpression: On.() -> Unit = { pending() })
}

public trait On: SkipSupport {

    fun it(description: String, itExpression: It.() -> Unit = { pending() })
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

    fun skip(why: String = "not given")

    fun pending(why: String = "not given")
}

/*
*  TODO: the parameter (why) could be optional but due to this bug (#KT-3197),
*        I had no choice but to make it mandatory for now.
*  TODO: need to be refactored when #KT-3197 got fixed.
*/
Retention(RetentionPolicy.RUNTIME) public annotation class skip(val why: String)

