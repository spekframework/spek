package org.spek

import java.util.ArrayList
import javax.security.auth.callback.TextOutputCallback
import java.io.Closeable

public class SpecificationRunner(val listener: Listener): Closeable {


    var totalPassed = 0
    var totalFailed = 0
    var totalIgnored = 0
    var totalError = 0

    val givenExecutedEvent = object: EventHandler {
        override fun handle(data: Any) {
            listener.notify(data as GivenExecuted)
        }

    }

    val onExecutedEvent = object: EventHandler {
        override fun handle(data: Any) {
            listener.notify(data as OnExecuted)
        }

    }

    val itExecutedEvent = object: EventHandler {
        override fun handle(data: Any) {
            totalPassed += 1
            listener.notify(data as ItExecuted)
        }

    }

    val assertionErrorOccurredEvent = object: EventHandler {
        override fun handle(data: Any) {
            totalFailed += 1
            listener.notify(data as AssertionErrorOccurred)
        }

    }



    {
        eventAggregator.subscribe("OnExecuted", onExecutedEvent)
        eventAggregator.subscribe("ItExecuted", itExecutedEvent)
        eventAggregator.subscribe("AssertionErrorOccurred", assertionErrorOccurredEvent)
        eventAggregator.subscribe("GivenExecuted", givenExecutedEvent)
    }

    fun resetCounters() {
        totalPassed = 0
        totalFailed = 0
        totalIgnored = 0
        totalError = 0
    }

    public override fun close() {
        eventAggregator.unSubscribe("OnExecuted", onExecutedEvent)
        eventAggregator.unSubscribe("ItExecuted", itExecutedEvent)
        eventAggregator.unSubscribe("AssertionErrorOccurred", assertionErrorOccurredEvent)
        eventAggregator.unSubscribe("GivenExecuted", givenExecutedEvent)
    }


    public fun runSpecs(folder: String, packageName: String) {
        resetCounters()
        val specFinder = SpecificationFinder()
        val specs = specFinder.getSpecifications(folder, packageName)
        listener.notify(RunStarted(specs.count()))
        if (specs.count() > 0) {
            specs forEach { it.run(listener)}
        }
        listener.notify(RunFinished(totalPassed, totalFailed, totalIgnored, totalError))
    }



}
