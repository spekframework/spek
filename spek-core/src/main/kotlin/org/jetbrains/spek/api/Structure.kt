package org.jetbrains.spek.api

interface Specification {
    fun given(description: String, givenExpression: Given.() -> Unit)
}

interface Given {
    fun on(description: String, onExpression: On.() -> Unit)
}

interface On {
    fun it(description: String, itExpression: It.() -> Unit)
}

interface It {}

