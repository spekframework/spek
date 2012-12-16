package org.spek

import java.util.ArrayList

public class MultipleListenerNotifier(val listeners: ArrayList<Listener>): Listener {

    override fun notify(runStarted: RunStarted) {
        listeners forEach { it.notify(runStarted)}
    }
    override fun notify(runFinished: RunFinished) {
        listeners forEach { it.notify(runFinished)}
    }
    override fun notify(onExecuted: OnExecuted) {
        listeners forEach { it.notify(onExecuted)}
    }
    override fun notify(itExecuted: ItExecuted) {
        listeners forEach { it.notify(itExecuted)}
    }
    override fun notify(specExecuted: SpecificationExecuted) {
        listeners forEach { it.notify(specExecuted)}
    }
    override fun notify(assertError: AssertionErrorOccurred) {
        listeners forEach { it.notify(assertError)}
    }
    override fun notify(givenExecuted: GivenExecuted) {
        listeners forEach { it.notify(givenExecuted)}
    }
    override fun notify(specError: SpecificationErrorOccurred) {
        listeners forEach { it.notify(specError)}
    }


}