package org.spek

public class SkippedException(message: String): RuntimeException(message)

public class PendingException(message: String): RuntimeException(message)