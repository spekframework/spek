package org.jetbrains.spek.junit

import org.jetbrains.spek.api.Notifier
import org.jetbrains.spek.api.SpekTree
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier

class JUnitNotifier(val runNotifier: RunNotifier, val junitDescriptionCache: JUnitDescriptionCache) : Notifier {
    override fun start(key: SpekTree) {
        runNotifier.fireTestStarted(junitDescriptionCache.get(key))
    }

    override fun succeed(key: SpekTree) {
        runNotifier.fireTestFinished(junitDescriptionCache.get(key))
    }

    override fun fail(key: SpekTree, error: Throwable) {
        val description = junitDescriptionCache.get(key)
        runNotifier.fireTestFailure(Failure(description, error))
        runNotifier.fireTestFinished(description)
    }

    override fun ignore(key: SpekTree) {
        val description = junitDescriptionCache.get(key)

        runNotifier.fireTestIgnored(description)
        runNotifier.fireTestFinished(description)
    }
}