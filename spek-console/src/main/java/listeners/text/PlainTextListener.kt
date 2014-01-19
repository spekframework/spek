package org.spek.console.listeners.text

import org.spek.impl.events.*
import org.spek.impl.StepListener
import org.spek.console.output.OutputDevice

public class PlainTextListener(output: OutputDevice): Listener, OutputDevice by output {
    override fun spek(spek: String): StepListener {
        return object : StepListener {
            override fun executionStarted() {
                output(spek)
            }
            override fun executionFailed(error: Throwable) {
                output("Failed: " + error.getMessage() + " " + error)
                output("")
            }
            override fun executionSkipped(why: String) {
                output("Skipped Spek '$spek'. Reason: $why")
            }
            override fun executionPending(why: String) {
                output("Pending Spek '$spek'. Reason: $why")
            }
        }
    }

    override fun given(given: String): StepListener {
        return object : StepListener {
            override fun executionStarted() {
                output("  Given $given")
            }
            override fun executionCompleted() {
                output("")
            }
            override fun executionSkipped(why: String) {
                output("  Skipped Given '$given'. Reason: $why")
            }
            override fun executionPending(why: String) {
                output("  Pending Given '$given'. Reason: $why")
            }
            override fun executionFailed(error: Throwable) {
                output("  Failed: $error")
                output("")
            }
        }
    }

    override fun on(given: String, on: String): StepListener {
        return object : StepListener {
            override fun executionStarted() {
                output("    On $on")
            }
            override fun executionFailed(error: Throwable) {
                output("    Failed: $error")
                output("")
            }
            override fun executionSkipped(why: String) {
                output("    Skipped On '$on'. Reason: $why")
            }
            override fun executionPending(why: String) {
                output("    Pending On '$on'. Reason: $why")
            }
        }
    }

    override fun it(given: String, on: String, it: String): StepListener {
        return object : StepListener {
            override fun executionStarted() {
                output("      It $it")
            }
            override fun executionFailed(error: Throwable) {
                output("      Failed: $error")
                output("")
            }
            override fun executionSkipped(why: String) {
                output("      Skipped It '$it'. Reason: $why")
            }
            override fun executionPending(why: String) {
                output("      Pending It '$it'. Reason: $why")
            }
        }
    }

}
