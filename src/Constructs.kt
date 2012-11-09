package spek

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import com.sun.javafx.binding.SelectBinding.AsString

Retention(RetentionPolicy.RUNTIME) annotation class spec

val eventAggregator = SimpleEventAggregator()

public class Given {
    public fun on(description: String, onExpression: On.() -> Unit) {
        eventAggregator.publish(OnExecuted(description))
        On().onExpression()
    }
}

public class On
{

    public fun it(description: String, itExpression: ()->Unit) {
        try {
            itExpression()
            eventAggregator.publish(ItExecuted(description))
        } catch (e: AssertionError) {
            eventAggregator.publish(AssertionErrorOccurred(description, e))
        }
    }
}


public fun given(description : String, givenExpression: Given.() -> Unit) {
    eventAggregator.publish(GivenExecuted(description))
    Given().givenExpression()
}
