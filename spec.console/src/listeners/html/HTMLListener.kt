package org.spek.console.listeners.html

import org.spek.*
import org.spek.impl.*
import org.spek.impl.events.*

//Not supported for now
/*
public class HTMLListener(output: OutputDevice, cssFile: String): Listener, OutputDevice by output {
    //TODO: could use markdown + js
    var css = ""

    {
        if (cssFile != "") {
            css = "<link rel=\"stylesheet\" type=\"text/css\" href=\"$cssFile\">"
        }

    }


    override fun given(given: String): StepListener {
        return object : StepListener {
            override fun executionStarted() {
                output("Given ${given}")
            }
            override fun executionCompleted() {
                output("")
            }
            override fun executionFailed(error: Throwable) {
                output("Failed: ${error}")
                output("")
            }
        }
    }

    override fun on(given: String, on: String): StepListener {
        return object : StepListener {
            override fun executionStarted() {
                output("  On ${on}")
            }
            override fun executionFailed(error: Throwable) {
                output("  Failed: ${error}")
                output("")
            }
        }
    }

    override fun it(given: String, on: String, it: String): StepListener {
        return object : StepListener {
            override fun executionStarted() {
                output("    It ${it}")
            }
            override fun executionFailed(error: Throwable) {
                output("    Failed: ${error}")
                output("")
            }
        }
    }



    override fun notify(onExecuted: OnExecuted) {
        output("<div class=\"on\">on ${onExecuted.description}</div>")
    }

    override fun notify(itExecuted: ItExecuted) {
        output("<div class=\"it\">it ${itExecuted.description}</div>")
    }

    override fun notify(specExecuted: SpecificationExecuted) {

    }

    override fun notify(assertError: AssertionErrorOccurred) {
        output("${assertError.description}")
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        assertError.error.printStackTrace(pw)
        output("<div class=\"assertError\"> ${sw.toString()}</div>")
    }

    override fun notify(givenExecuted: GivenExecuted) {
        output("<div class=\"given\">Given ${givenExecuted.description}</div>")
    }

    override fun notify(specError: SpecificationErrorOccurred) {
        output("<div class=\"specError\">Error running specification: ${specError.errorMessage}</div>")
    }

    override fun notify(runStarted: RunStarted) {
        output("<html><head><title>Specs</title>$css<body><div class=\"specs\">")
        output("<div class=\"runStarted\">Found <div class=\"totalSpecs\">${runStarted.totalSpecifications} specification(s)<div></div>")
    }

    override fun notify(runFinished: RunFinished) {
        output("<div class=\"runFinished\"><div class=\"totalPassed\">Total passed: ${runFinished.passed}</div> <div class=\"totalFailed\">failed: ${runFinished.failed}</div></div></div></body></html>")
    }

}
*/
