package org.jetbrains.spek.junit

import org.jetbrains.spek.api.Notifier
import org.jetbrains.spek.api.TestAction
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier

class JUnitNotifier(val runNotifier: RunNotifier, val junitDescriptionCache: JUnitDescriptionCache) : Notifier {
    override fun start(key: TestAction) {
        println("start: " + key.description() + "\n")
        System.out.flush()
        runNotifier.fireTestStarted(junitDescriptionCache.get(key))
    }

    override fun succeed(key: TestAction) {
        println("succeed: " + key.description() + "\n")
        System.out.flush()
        runNotifier.fireTestFinished(junitDescriptionCache.get(key))
    }

    override fun fail(key: TestAction, error: Throwable) {
        println("fail: " + key.description() + "\n")
        System.out.flush()
        val description = junitDescriptionCache.get(key)
        runNotifier.fireTestFailure(Failure(description, error))
        runNotifier.fireTestFinished(description)
    }

    override fun ignore(key: TestAction) {
        println("ignore: " + key.description() + "\n")
        System.out.flush()
        val description = junitDescriptionCache.get(key)

        runNotifier.fireTestIgnored(description)
        runNotifier.fireTestFinished(description)
    }
}