package spek

import java.io.StringWriter
import java.io.PrintWriter


public class ConsoleTextListener: Listener {

    override fun notify(onExecuted: OnExecuted) {
        println("On ${onExecuted.description}")
    }

    override fun notify(itExecuted: ItExecuted) {
        println("It ${itExecuted.description}")
    }

    override fun notify(specExecuted: SpecificationExecuted) {

    }

    override fun notify(assertError: AssertionErrorOccurred) {
        println("Failed ${assertError.description}")
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        assertError.error.printStackTrace(pw)
        println(sw.toString())
    }

    override fun notify(givenExecuted: GivenExecuted) {
        println("Given ${givenExecuted.description}")
    }

    override fun notify(specError: SpecificationErrorOccurred) {
        println("Error running specification: ${specError.errorMessage}")
    }

    override fun notify(runStarted: RunStarted) {
        println("Found ${runStarted.totalSpecifications} specification. Starting run")
    }

    override fun notify(runFinished: RunFinished) {
        println("Finished. Total passed: ${runFinished.passed} failed: ${runFinished.failed}")
    }
}