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

public trait It {
    fun shouldEqual<T>(expected: T, actual: T)
    fun shouldNotEqual<T>(expected: T, actual: T)
    fun shouldBeNull<T>(actual: T)
    fun shouldNotBeNull<T>(actual: T)
    fun shouldBeTrue<T>(actual: T)
    fun shouldBeFalse<T>(actual: T)
}

trait SkipSupport {
    fun skip(why: String)
    fun skip()

    fun pending(why: String)
    fun pending()
}
