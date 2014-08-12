package org.jetbrains.spek.api

public class SkippedException(message: String): RuntimeException(message)

public class PendingException(message: String): RuntimeException(message)