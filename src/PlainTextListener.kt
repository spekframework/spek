package spek

import java.io.StringWriter
import java.io.PrintWriter


public class PlainTextListener(output: Device): Listener, Device by output {

    override fun notify(onExecuted: OnExecuted) {
        output("  On ${onExecuted.description}")
    }

    override fun notify(itExecuted: ItExecuted) {
        output("    It ${itExecuted.description}")
    }

    override fun notify(specExecuted: SpecificationExecuted) {

    }

    override fun notify(assertError: AssertionErrorOccurred) {
        output("${assertError.description}")
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        assertError.error.printStackTrace(pw)
        output(sw.toString())
    }

    override fun notify(givenExecuted: GivenExecuted) {
        output("Given ${givenExecuted.description}")
    }

    override fun notify(specError: SpecificationErrorOccurred) {
        output("Error running specification: ${specError.errorMessage}")
    }

    override fun notify(runStarted: RunStarted) {
        output("Found ${runStarted.totalSpecifications} specification(s)\n\n")
    }

    override fun notify(runFinished: RunFinished) {
        output("\n\nTotal passed: ${runFinished.passed} failed: ${runFinished.failed}")
    }
}