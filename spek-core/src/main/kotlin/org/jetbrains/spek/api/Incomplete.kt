package org.jetbrains.spek.api

public fun Specification.given(description: String): Unit = given(description) { pending("Not implemented.") }
public fun Given.on(description: String): Unit = on(description) { pending("Not implemented.") }
public fun On.it(description: String): Unit = it(description) { pending("Not implemented.") }

public fun pending(message : String) : Unit = throw PendingException(message)
public fun skip(message : String) : Unit = throw SkippedException(message)



