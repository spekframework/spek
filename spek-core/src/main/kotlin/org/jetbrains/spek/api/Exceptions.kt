package org.jetbrains.spek.api

class SkippedException(message: String) : RuntimeException(message)

class PendingException(message: String) : RuntimeException(message)