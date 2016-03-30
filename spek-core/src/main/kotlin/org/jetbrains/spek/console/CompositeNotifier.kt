package org.jetbrains.spek.console

import org.jetbrains.spek.api.SpekTree

class CompositeNotifier : ConsoleNotifier {
    val notifiers: MutableList<ConsoleNotifier> = mutableListOf()

    fun add(notifier: ConsoleNotifier) {
        notifiers.add(notifier)
    }

    override fun start(key: SpekTree) {
        notifiers.forEach { it.start(key) }
    }

    override fun succeed(key: SpekTree) {
        notifiers.forEach { it.succeed(key) }
    }

    override fun fail(key: SpekTree, error: Throwable) {
        notifiers.forEach { it.fail(key, error) }
    }

    override fun ignore(key: SpekTree) {
        notifiers.forEach { it.ignore(key) }
    }

    override fun finish() {
        notifiers.forEach { it.finish() }
    }
}