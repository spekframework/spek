package spek

import java.util.ArrayList

public class MultipleListenerNotifier(val outputs: ArrayList<Listener>): Listener {

    override fun notify(runStarted: RunStarted) {
        for (val output in outputs.iterator()) {
            output.notify(runStarted)
        }
    }
    override fun notify(runFinished: RunFinished) {
        for (val output in outputs.iterator()) {
            output.notify(runFinished)
        }
    }
    override fun notify(onExecuted: OnExecuted) {
        for (val output in outputs.iterator()) {
            output.notify(onExecuted)
        }
    }
    override fun notify(itExecuted: ItExecuted) {
        for (val output in outputs.iterator()) {
            output.notify(itExecuted)
        }
    }
    override fun notify(specExecuted: SpecificationExecuted) {
        for (val output in outputs.iterator()) {
            output.notify(specExecuted)
        }
    }
    override fun notify(assertError: AssertionErrorOccurred) {
        for (val output in outputs.iterator()) {
            output.notify(assertError)
        }
    }
    override fun notify(givenExecuted: GivenExecuted) {
        for (val output in outputs.iterator()) {
            output.notify(givenExecuted)
        }
    }
    override fun notify(specError: SpecificationErrorOccurred) {
        for (val output in outputs.iterator()) {
            output.notify(specError)
        }
    }


}