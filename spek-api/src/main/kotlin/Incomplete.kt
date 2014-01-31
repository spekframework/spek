package org.spek.api

public fun Specification.given(description: String): Unit = pending(description)
public fun Given.on(description: String): Unit = pending(description)
public fun On.it(description: String): Unit = pending(description)

public fun It.skip(description: String): Unit = skip(description)
public fun On.skip(description: String): Unit = skip(description)
public fun Given.skip(description: String): Unit = skip(description)

public fun pending(message : String) : Unit = throw PendingException(message)
public fun skip(message : String) : Unit = throw SkippedException(message)