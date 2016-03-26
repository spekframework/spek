package org.jetbrains.spek.api

interface Notifier {
    fun start(key: TestAction)
    fun succeed(key: TestAction)
    fun fail(key: TestAction, error: Throwable)
    fun ignore(key: TestAction)
}