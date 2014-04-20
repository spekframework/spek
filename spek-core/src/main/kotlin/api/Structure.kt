package org.spek

public trait Specification {
    fun given(description: String, givenExpression: Given.() -> Unit)
}

public trait Given {
    fun beforeOn(it: () -> Unit)
    fun afterOn(it: () -> Unit)
    fun on(description: String, onExpression: On.() -> Unit)
}

public trait On {
    fun it(description: String, itExpression: It.() -> Unit)
}

public trait It {}

