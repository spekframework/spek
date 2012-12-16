package org.spek

public class Given {
    public fun on(description: String, onExpression: On.() -> Unit) {
        eventAggregator.publish(OnExecuted(description))
        On().onExpression()
    }
}
