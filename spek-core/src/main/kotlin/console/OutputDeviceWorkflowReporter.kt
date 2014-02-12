package org.spek.console

public class OutputDeviceWorkflowReporter(val device: OutputDevice) : WorkflowReporter {
    override fun spek(spek: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                device.output(spek)
            }
            override fun failed(error: Throwable) {
                device.output("Failed: " + error.getMessage() + " " + error)
                device.output("")
            }
            override fun skipped(why: String) {
                device.output("Skipped Spek '$spek'. Reason: $why")
            }
            override fun pending(why: String) {
                device.output("Pending Spek '$spek'. Reason: $why")
            }
        }
    }

    override fun given(spek: String, given: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                device.output("  Given $given")
            }
            override fun completed() {
                device.output("")
            }
            override fun skipped(why: String) {
                device.output("  Skipped Given '$given'. Reason: $why")
            }
            override fun pending(why: String) {
                device.output("  Pending Given '$given'. Reason: $why")
            }
            override fun failed(error: Throwable) {
                device.output("  Failed: $error")
                device.output("")
            }
        }
    }

    override fun on(spek: String, given: String, on: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                device.output("    On $on")
            }
            override fun failed(error: Throwable) {
                device.output("    Failed: $error")
                device.output("")
            }
            override fun skipped(why: String) {
                device.output("    Skipped On '$on'. Reason: $why")
            }
            override fun pending(why: String) {
                device.output("    Pending On '$on'. Reason: $why")
            }
        }
    }

    override fun it(spek: String, given: String, on: String, it: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                device.output("      It $it")
            }
            override fun failed(error: Throwable) {
                device.output("      Failed: $error")
                device.output("")
            }
            override fun skipped(why: String) {
                device.output("      Skipped It '$it'. Reason: $why")
            }
            override fun pending(why: String) {
                device.output("      Pending It '$it'. Reason: $why")
            }
        }
    }

}
