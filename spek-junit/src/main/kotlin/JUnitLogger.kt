package org.spek.junit.impl

import org.junit.runner.Description
import org.spek.impl.ExecutionReporter
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import org.spek.impl.events.Listener

public class JUnitLogger(val notifier: RunNotifier, val spekDescription: Description): Listener {
    override fun spek(spek: String): ExecutionReporter {

        return object : ExecutionReporter {
            override fun started() {
                notifier.fireTestStarted(spekDescription)
            }

            override fun completed() {
                notifier.fireTestFinished(spekDescription)
            }

            override fun skipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "Skipped. Reason: $why"))
            }
        }
    }

    override fun given(spek: String, given: String): ExecutionReporter {
        val desc = Description.createTestDescription(spek, given)

        return object : ExecutionReporter {
            override fun started() {
                notifier.fireTestStarted(desc)
            }

            override fun completed() {
                notifier.fireTestFinished(desc)
            }

            override fun skipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given skipped. Reason: $why"))
            }
            override fun pending(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given pending. Reason: $why"))
            }
            override fun failed(error: Throwable) {
                notifier.fireTestFailure(Failure(desc, error))
            }
        }
    }

    override fun on(spek: String, given: String, on: String): ExecutionReporter {
        val desc = Description.createTestDescription(spek, "$given, $on")

        return object : ExecutionReporter {
            override fun started() {
                notifier.fireTestStarted(desc)
            }

            override fun completed() {
                notifier.fireTestFinished(desc)
            }

            override fun skipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given, $on skipped. Reason: $why"))

            }
            override fun pending(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given, $on pending. Reason: $why"))

            }
            override fun failed(error: Throwable) {
                notifier.fireTestFailure(Failure(
                        desc,
                        error))
            }
        }
    }

    override fun it(spek: String, given: String, on: String, it: String): ExecutionReporter {

        val desc = Description.createTestDescription(spek, "$given, $on, $it")

        return object : ExecutionReporter {
            override fun started() {
                notifier.fireTestStarted(desc)
            }

            override fun completed() {
                notifier.fireTestFinished(desc)
            }

            override fun skipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given, $on, $it skipped. Reason: $why"))
            }

            override fun pending(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given, $on,  $it pending. Reason: $why"))
            }

            override fun failed(error: Throwable) {
                notifier.fireTestFailure(Failure(desc, error))
            }
        }
    }
}
