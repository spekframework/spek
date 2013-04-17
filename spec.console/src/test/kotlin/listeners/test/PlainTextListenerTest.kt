package org.spek.console.listeners.text

import org.junit.Test as test
import kotlin.test.assertTrue
import org.mockito.Mockito

public class TextListenerTest {
    val device = Mockito.mock(javaClass<OutputDevice>())!!
    val textListener = PlainTextListener(device)

    test fun given() {
        //given a text listener and a step listener that is bound to it.
        val listener = textListener.given("A Given")

        //when execution started
        listener.executionStarted()
        //then a log message is written to output device
        Mockito.verify(device)!!.output("Given A Given")

        //when execution completed
        listener.executionCompleted()
        //then a log message is written to output device
        Mockito.verify(device)!!.output("")

        //when execution failed
        listener.executionFailed(RuntimeException())
        //then a log message is written to output device
        Mockito.verify(device)!!.output("Failed: java.lang.RuntimeException")
    }

    test fun on() {
        //given a text listener and a step listener that is bound to it.
        val listener = textListener.on("A Given", "An On")

        //when execution started
        listener.executionStarted()
        //then a log message is written to output device
        Mockito.verify(device)!!.output("  On An On")

        //when execution failed
        listener.executionFailed(RuntimeException())
        //then a log message is written to output device
        Mockito.verify(device)!!.output("  Failed: java.lang.RuntimeException")
    }

    test fun it() {
        //given a text listener and a step listener that is bound to it.
        val listener = textListener.it("A Given", "An On", "An It")

        //when execution started
        listener.executionStarted()
        //then a log message is written to output device
        Mockito.verify(device)!!.output("    It An It")

        //when execution failed
        listener.executionFailed(RuntimeException())
        //then a log message is written to output device
        Mockito.verify(device)!!.output("    Failed: java.lang.RuntimeException")
    }
}