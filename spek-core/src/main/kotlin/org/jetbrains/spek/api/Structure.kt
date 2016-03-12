package org.jetbrains.spek.api

interface Specification {
    fun given(description: String, givenExpression: Given.() -> Unit)
    fun beforeEach(action: () -> Unit)
    fun afterEach(action: () -> Unit)
}

interface Given {
    fun beforeOn(it: () -> Unit)
    fun afterOn(it: () -> Unit)
    fun on(description: String, onExpression: On.() -> Unit)
}

interface On {
    fun it(description: String, itExpression: It.() -> Unit)
}

interface It {}

