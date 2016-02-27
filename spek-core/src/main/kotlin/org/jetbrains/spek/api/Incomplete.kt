package org.jetbrains.spek.api

fun Specification.given(description: String): Unit = given(description) { pending("Not implemented.") }
fun Given.on(description: String): Unit = on(description) { pending("Not implemented.") }
fun On.it(description: String): Unit = it(description) { pending("Not implemented.") }

fun pending(message: String): Unit = throw PendingException(message)
fun skip(message: String): Unit = throw SkippedException(message)



