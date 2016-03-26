package org.jetbrains.spek.console

import org.jetbrains.spek.api.TestAction

class CompositeNotifier : ConsoleNotifier {
    val notifiers: MutableList<ConsoleNotifier> = mutableListOf()

    fun add(notifier: ConsoleNotifier) {
        notifiers.add(notifier)
    }

    override fun start(key: TestAction) {
        notifiers.forEach { it.start(key) }
    }

    override fun succeed(key: TestAction) {
        notifiers.forEach { it.succeed(key) }
    }

    override fun fail(key: TestAction, error: Throwable) {
        notifiers.forEach { it.fail(key, error) }
    }

    override fun ignore(key: TestAction) {
        notifiers.forEach { it.ignore(key) }
    }

    override fun finish() {
        notifiers.forEach { it.finish() }
    }
}