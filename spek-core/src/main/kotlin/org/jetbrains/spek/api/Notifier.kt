package org.jetbrains.spek.api

interface Notifier {
    fun start(key: SpekTree)
    fun succeed(key: SpekTree)
    fun fail(key: SpekTree, error: Throwable)
    fun ignore(key: SpekTree)
}