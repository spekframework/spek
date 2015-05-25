package org.jetbrains.spek.console

public class HtmlWorkflowReporter(val suite: String, val device: OutputDevice, val cssFile: String) : WorkflowReporter {
    //TODO: could use markdown + js
    var css = ""

    init {
        if (cssFile != "") {
            css = "<link rel=\"stylesheet\" type=\"text/css\" href=\"$cssFile\">"
        }
        device.output("<html><head><title>$suite</title>$css<body><div class=\"suite\">")
    }

    override fun spek(spek: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                //device.output("<div class=\"runStarted\">Found <div class=\"totalSpecs\">${runStarted.totalSpecifications} specification(s)<div></div>")
                device.output("<div class=\"spek\"><h1>${spek}</h1>")
            }
            override fun completed() {
                device.output("</div>")
                //device.output("<div class=\"runFinished\"><div class=\"totalPassed\">Total passed: ${runFinished.passed}</div> <div class=\"totalFailed\">failed: ${runFinished.failed}</div></div></div></body></html>")
            }
            override fun failed(error: Throwable) {
                device.output("Failed: ${error}")
                device.output("")
            }
        }
    }

    override fun given(spek: String, given: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                device.output("  <div class=\"given\"><h2>${given}</h2>")
            }
            override fun completed() {
                device.output("  </div>")
            }
            override fun failed(error: Throwable) {
                device.output("  Failed: ${error}")
                device.output("")
            }
        }
    }

    override fun on(spek: String, given: String, on: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                device.output("    <div class=\"on\"><h3>${on}</h3>")
            }
            override fun completed() {
                device.output("    </div>")
            }
            override fun failed(error: Throwable) {
                device.output("    Failed: ${error}")
                device.output("")
            }
        }
    }

    override fun it(spek: String, given: String, on: String, it: String): ActionStatusReporter {
        return object : ActionStatusReporter {
            override fun started() {
                device.output("      <div class=\"it\"><h4>${it}</h4>")
            }
            override fun completed() {
                device.output("      </div>")
            }
            override fun failed(error: Throwable) {
                device.output("      Failed: ${error}")
                device.output("")
            }
        }
    }
}
