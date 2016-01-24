package org.jetbrains.spek.api

public interface Specification {
    public fun given(description: String, givenExpression: Given.() -> Unit)
}

public interface Given {
    public fun on(description: String, onExpression: On.() -> Unit)
}

public interface On {
    public fun it(description: String, itExpression: It.() -> Unit)
}

public interface It {}

