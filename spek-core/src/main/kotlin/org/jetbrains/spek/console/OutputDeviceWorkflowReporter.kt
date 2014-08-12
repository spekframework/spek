package org.jetbrains.spek.console

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
                device.output("Skipped '$spek'. Reason: $why")
            }
            override fun pending(why: String) {
                device.output("Pending '$spek'. Reason: $why")
            }
        }
    }

    override fun given(spek: String, given: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                device.output("  $given")
            }
            override fun completed() {
                device.output("")
            }
            override fun skipped(why: String) {
                device.output("  Skipped '$given'. Reason: $why")
            }
            override fun pending(why: String) {
                device.output("  Pending '$given'. Reason: $why")
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
                device.output("    $on")
            }
            override fun failed(error: Throwable) {
                device.output("    Failed: $error")
                device.output("")
            }
            override fun skipped(why: String) {
                device.output("    Skipped '$on'. Reason: $why")
            }
            override fun pending(why: String) {
                device.output("    Pending '$on'. Reason: $why")
            }
        }
    }

    override fun it(spek: String, given: String, on: String, it: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                device.output("      $it")
            }
            override fun failed(error: Throwable) {
                device.output("      Failed: $error")
                device.output("")
            }
            override fun skipped(why: String) {
                device.output("      Skipped '$it'. Reason: $why")
            }
            override fun pending(why: String) {
                device.output("      Pending '$it'. Reason: $why")
            }
        }
    }

}
