package spek

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

Retention(RetentionPolicy.RUNTIME) annotation class spec

val eventAggregator = SimpleEventAggregator()

fun on(description: String, onExpression: () -> Unit) {
    eventAggregator.publish(OnExecuted(description))
    onExpression()
}

fun it(description: String, itExpression: () -> Unit) {
    try {
        itExpression()
        eventAggregator.publish(ItExecuted(description))
    } catch (e: AssertionError) {
        eventAggregator.publish(AssertionErrorOccurred(description, e))
    }
}

fun given(description: String, givenExpression: () -> Unit) {
    eventAggregator.publish(GivenExecuted(description))
    givenExpression()
}
