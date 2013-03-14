package org.spek

public class On {

    public fun it(description: String, itExpression: It.()->Unit) {
        try {
            It().itExpression()
            eventAggregator.publish(ItExecuted(description))
        } catch (e: AssertionError) {
            eventAggregator.publish(AssertionErrorOccurred(description, e))
        }
    }
}
