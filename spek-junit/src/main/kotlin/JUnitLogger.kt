package org.spek.junit.impl

import org.junit.runner.Description
import org.spek.impl.StepListener
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import org.spek.impl.events.Listener

public class JUnitLogger(val notifier: RunNotifier): Listener {
    override fun spek(spek: String): StepListener {
        val desc = Description.createSuiteDescription(spek)

        return object : StepListener {
            override fun executionStarted() {
                notifier.fireTestStarted(desc)
            }

            override fun executionCompleted() {
                notifier.fireTestFinished(desc)
            }

            override fun executionSkipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "Skipped. Reason: $why"))
            }
        }
    }

    override fun given(spek: String, given: String): StepListener {
        val desc = Description.createTestDescription(spek, given)

        return object : StepListener {
            override fun executionStarted() {
                notifier.fireTestStarted(desc)
            }

            override fun executionCompleted() {
                notifier.fireTestFinished(desc)
            }

            override fun executionSkipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given skipped. Reason: $why"))
            }
            override fun executionPending(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given pending. Reason: $why"))
            }
            override fun executionFailed(error: Throwable) {
                notifier.fireTestFailure(Failure(desc, error))
            }
        }
    }

    override fun on(spek: String, given: String, on: String): StepListener {
        val desc = Description.createTestDescription(spek, "$given, $on")

        return object : StepListener {
            override fun executionStarted() {
                notifier.fireTestStarted(desc)
            }

            override fun executionCompleted() {
                notifier.fireTestFinished(desc)
            }

            override fun executionSkipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given, $on skipped. Reason: $why"))

            }
            override fun executionPending(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given, $on pending. Reason: $why"))

            }
            override fun executionFailed(error: Throwable) {
                notifier.fireTestFailure(Failure(
                        desc,
                        error))
            }
        }
    }

    override fun it(spek: String, given: String, on: String, it: String): StepListener {

        val desc = Description.createTestDescription(spek, "$given, $on, $it")

        return object : StepListener {
            override fun executionStarted() {
                notifier.fireTestStarted(desc)
            }

            override fun executionCompleted() {
                notifier.fireTestFinished(desc)
            }

            override fun executionSkipped(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given, $on, $it skipped. Reason: $why"))
            }

            override fun executionPending(why: String) {
                notifier.fireTestIgnored(Description
                        .createTestDescription(spek, "$given, $on,  $it pending. Reason: $why"))
            }

            override fun executionFailed(error: Throwable) {
                notifier.fireTestFailure(Failure(desc, error))
            }
        }
    }
}
