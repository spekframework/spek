package org.spek.api

class SkippedException(message: String): RuntimeException(message)

class PendingException(message: String): RuntimeException(message)