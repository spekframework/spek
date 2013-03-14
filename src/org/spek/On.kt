package org.spek

public class On {

    public fun it(description: String, itExpression: It.()->Unit) {
        eventAggregator.publish(ItStarted(description))
        try {
            It().itExpression()
            eventAggregator.publish(ItExecuted(description))
        } catch (e: AssertionError) {
            eventAggregator.publish(AssertionErrorOccurred(description, e))
        }
        eventAggregator.publish(ItCompleted(description))
    }
}
