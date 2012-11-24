

import spek.Listener
import spek.*
import java.io.StringWriter
import java.io.PrintWriter

public class HTMLListener(output: OutputDevice): Listener, OutputDevice by output {
    {
        output("<html><head><title>Specs</title><css link><body><div class=\"specs\">")

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
        output("<div class=\"runStarted\">Found <div class=\"totalSpecs\">${runStarted.totalSpecifications} specification(s)<div></div>")
    }

    override fun notify(runFinished: RunFinished) {
        output("<div class=\"runFinished\"><div class=\"totalPassed\">Total passed: ${runFinished.passed}</div> <div class=\"totalFailed\">failed: ${runFinished.failed}</div></div></div></body></html>")
    }

}