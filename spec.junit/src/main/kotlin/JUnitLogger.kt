package org.spek.junit.impl

import org.junit.runner.Description
import org.spek.impl.StepListener
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import org.spek.impl.events.Listener

public class JUnitLogger(val notifier: RunNotifier): Listener {
    override fun spek(spek: String): StepListener {
        return object : StepListener {
            override fun executionSkipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription("$spek", "Skipped. Reason: $why"))
            }
        }
    }

    override fun given(given: String): StepListener {
        return object : StepListener {
            override fun executionSkipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription("$given", "Skipped. Reason: $why"))
            }
            override fun executionPending(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription("$given", "Pending. Reason: $why"))
            }
            override fun executionFailed(error: Throwable) {
                notifier.fireTestFailure(Failure(Description
                        .createTestDescription("$given", "given"), error))
            }
        }
    }

    override fun on(given: String, on: String): StepListener {

        return object : StepListener {

            override fun executionSkipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription("$given, $on", "Skipped. Reason: $why"))

            }
            override fun executionPending(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription("$given, $on", "Pending. Reason: $why"))

            }
            override fun executionFailed(error: Throwable) {
                notifier.fireTestFailure(Failure(
                        Description.createTestDescription("$given, $on", "on"),
                        error))
            }
        }
    }

    override fun it(given: String, on: String, it: String): StepListener {

        val desc = Description.createTestDescription("$given, $on", it)

        return object : StepListener {
            override fun executionStarted() {
                notifier.fireTestStarted(desc)
            }

            override fun executionCompleted() {
                notifier.fireTestFinished(desc)
            }

            override fun executionSkipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription("$given, $on, $it", "Skipped. Reason: $why"))
            }

            override fun executionPending(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription("$given, $on,  $it", "Pending. Reason: $why"))
            }

            override fun executionFailed(error: Throwable) {
                notifier.fireTestFailure(Failure(desc, error))
            }
        }
    }
}
