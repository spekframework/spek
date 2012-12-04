package spek.test

import org.junit.Test as test
import kotlin.test.assertEquals
import spek.*

class SomeEventHandler: EventHandler {
    override fun  handle(data: Any) {
    }

}
public class TestSimpleEventAggregator {

    test fun can_subscribe_multiple_objects_to_one_event() {

        val simpleEventAggregator = SimpleEventAggregator()

        val obj1 = SomeEventHandler()
        val obj2 = SomeEventHandler()

        simpleEventAggregator.subscribe("RunStarted", obj1)
        simpleEventAggregator.subscribe("RunStarted", obj2)

        assertEquals(2, simpleEventAggregator.subscribers["RunStarted"]?.count())



    }

    test fun can_subscribe_multiple_objects_to_different_event() {

        val simpleEventAggregator = SimpleEventAggregator()

        val obj1 = SomeEventHandler()
        val obj2 = SomeEventHandler()
        val obj3 = SomeEventHandler()

        simpleEventAggregator.subscribe("RunStarted", obj1)
        simpleEventAggregator.subscribe("RunStarted", obj2)
        simpleEventAggregator.subscribe("RunFinished", obj3)

        assertEquals(2, simpleEventAggregator.subscribers["RunStarted"]?.count())
        assertEquals(1, simpleEventAggregator.subscribers["RunFinished"]?.count())



    }

    test fun can_unsubscribe_an_object_from_an_event() {
        val simpleEventAggregator = SimpleEventAggregator()

        val obj1 = SomeEventHandler()

        simpleEventAggregator.subscribe("RunStarted", obj1)
        simpleEventAggregator.unSubscribe("RunStarted", obj1)
        assertEquals(0, simpleEventAggregator.subscribers["RunStarted"]?.count())

    }


}