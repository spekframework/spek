package org.spek.console.listeners.html

import org.spek.*
import org.spek.impl.*
import org.spek.impl.events.*

//Not supported for now
/*
public class HTMLListener(main.output: OutputDevice, cssFile: String): Listener, OutputDevice by main.output {
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
                main.output("Given ${given}")
            }
            override fun executionCompleted() {
                main.output("")
            }
            override fun executionFailed(error: Throwable) {
                main.output("Failed: ${error}")
                main.output("")
            }
        }
    }

    override fun on(given: String, on: String): StepListener {
        return object : StepListener {
            override fun executionStarted() {
                main.output("  On ${on}")
            }
            override fun executionFailed(error: Throwable) {
                main.output("  Failed: ${error}")
                main.output("")
            }
        }
    }

    override fun it(given: String, on: String, it: String): StepListener {
        return object : StepListener {
            override fun executionStarted() {
                main.output("    It ${it}")
            }
            override fun executionFailed(error: Throwable) {
                main.output("    Failed: ${error}")
                main.output("")
            }
        }
    }



    override fun notify(onExecuted: OnExecuted) {
        main.output("<div class=\"on\">on ${onExecuted.description}</div>")
    }

    override fun notify(itExecuted: ItExecuted) {
        main.output("<div class=\"it\">it ${itExecuted.description}</div>")
    }

    override fun notify(specExecuted: SpecificationExecuted) {

    }

    override fun notify(assertError: AssertionErrorOccurred) {
        main.output("${assertError.description}")
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        assertError.error.printStackTrace(pw)
        main.output("<div class=\"assertError\"> ${sw.toString()}</div>")
    }

    override fun notify(givenExecuted: GivenExecuted) {
        main.output("<div class=\"given\">Given ${givenExecuted.description}</div>")
    }

    override fun notify(specError: SpecificationErrorOccurred) {
        main.output("<div class=\"specError\">Error running specification: ${specError.errorMessage}</div>")
    }

    override fun notify(runStarted: RunStarted) {
        main.output("<html><head><title>Specs</title>$css<body><div class=\"specs\">")
        main.output("<div class=\"runStarted\">Found <div class=\"totalSpecs\">${runStarted.totalSpecifications} specification(s)<div></div>")
    }

    override fun notify(runFinished: RunFinished) {
        main.output("<div class=\"runFinished\"><div class=\"totalPassed\">Total passed: ${runFinished.passed}</div> <div class=\"totalFailed\">failed: ${runFinished.failed}</div></div></div></body></html>")
    }

}
*/
