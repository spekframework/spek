package spek

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import com.sun.javafx.binding.SelectBinding.AsString

Retention(RetentionPolicy.RUNTIME) annotation class spec

val eventAggregator = SimpleEventAggregator()

class YOn<TGiven, TOn>
{
  fun yit(d: String, it: TOn.() -> Unit)
    {

    }
}

class YGiven<TGiven>
{
    fun <TOn> yon(d: String, on: TGiven.() -> TOn) : YOn<TGiven, TOn>
    {

    }

}

fun <TGiven> ygiven(d : String, give: () -> TGiven) : YGiven<TGiven>
{

}


class TheOn
{

    fun xit(d: String, should: ()->Unit)
    {

    }
}

class TheGiven
{
    public fun xon(description: String, onExpression: TheOn.() -> Unit)
    {

    }
}

fun xgiven(description : String, givenExpression: TheGiven.() -> Unit)
{
    TheGiven().givenExpression()



}


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
