package org.spek.console.listeners.text

import org.spek.impl.events.*
import org.spek.impl.ExecutionReporter
import org.spek.console.output.OutputDevice

public class PlainTextListener(output: OutputDevice): Listener, OutputDevice by output {
    override fun spek(spek: String): ExecutionReporter {
        return object : ExecutionReporter {
            override fun started() {
                output(spek)
            }
            override fun failed(error: Throwable) {
                output("Failed: " + error.getMessage() + " " + error)
                output("")
            }
            override fun skipped(why: String) {
                output("Skipped Spek '$spek'. Reason: $why")
            }
            override fun pending(why: String) {
                output("Pending Spek '$spek'. Reason: $why")
            }
        }
    }

    override fun given(spek: String, given: String): ExecutionReporter {
        return object : ExecutionReporter {
            override fun started() {
                output("  Given $given")
            }
            override fun completed() {
                output("")
            }
            override fun skipped(why: String) {
                output("  Skipped Given '$given'. Reason: $why")
            }
            override fun pending(why: String) {
                output("  Pending Given '$given'. Reason: $why")
            }
            override fun failed(error: Throwable) {
                output("  Failed: $error")
                output("")
            }
        }
    }

    override fun on(spek: String, given: String, on: String): ExecutionReporter {
        return object : ExecutionReporter {
            override fun started() {
                output("    On $on")
            }
            override fun failed(error: Throwable) {
                output("    Failed: $error")
                output("")
            }
            override fun skipped(why: String) {
                output("    Skipped On '$on'. Reason: $why")
            }
            override fun pending(why: String) {
                output("    Pending On '$on'. Reason: $why")
            }
        }
    }

    override fun it(spek: String, given: String, on: String, it: String): ExecutionReporter {
        return object : ExecutionReporter {
            override fun started() {
                output("      It $it")
            }
            override fun failed(error: Throwable) {
                output("      Failed: $error")
                output("")
            }
            override fun skipped(why: String) {
                output("      Skipped It '$it'. Reason: $why")
            }
            override fun pending(why: String) {
                output("      Pending It '$it'. Reason: $why")
            }
        }
    }

}
