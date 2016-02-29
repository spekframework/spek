package org.jetbrains.spek.junit

import org.jetbrains.spek.api.Notifier
import org.jetbrains.spek.api.TestAction
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier

class JUnitNotifier(val runNotifier: RunNotifier, val junitDescriptionCache: JUnitDescriptionCache) : Notifier {
    override fun start(key: TestAction) {
        runNotifier.fireTestStarted(junitDescriptionCache.get(key))
    }

    override fun succeed(key: TestAction) {
        runNotifier.fireTestFinished(junitDescriptionCache.get(key))
    }

    override fun fail(key: TestAction, error: Throwable) {
        val description = junitDescriptionCache.get(key)
        runNotifier.fireTestFailure(Failure(description, error))
        runNotifier.fireTestFinished(description)
    }

    override fun ignore(key: TestAction) {
        val description = junitDescriptionCache.get(key)

        runNotifier.fireTestIgnored(description)
        runNotifier.fireTestFinished(description)
    }
}