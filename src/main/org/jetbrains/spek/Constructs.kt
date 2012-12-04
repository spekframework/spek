package spek

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.test.assertEquals

Retention(RetentionPolicy.RUNTIME) annotation class spec

val eventAggregator = SimpleEventAggregator()

public fun given(description : String, givenExpression: Given.() -> Unit) {
    eventAggregator.publish(GivenExecuted(description))
    Given().givenExpression()
}


