package org.jetbrains.spek.api

public trait Specification {
    public fun given(description: String, givenExpression: Given.() -> Unit)
}

public trait Given {
    public fun beforeOn(it: () -> Unit)
    public fun afterOn(it: () -> Unit)
    public fun on(description: String, onExpression: On.() -> Unit)
}

public trait On {
    public fun it(description: String, itExpression: It.() -> Unit)
}

public trait It {}

