package org.spek.console.listeners.text

import org.junit.Test as test
import kotlin.test.assertTrue
import org.mockito.Mockito
import org.spek.console.output.OutputDevice

public class TextListenerTest {
    val device = Mockito.mock(javaClass<OutputDevice>())!!
    val textListener = PlainTextListener(device)

    test fun given() {
        //given a text listener and a step listener that is bound to it.
        val listener = textListener.given("Spek", "A Given")

        //when execution started
        listener.started()
        //then a log message is written to output device
        Mockito.verify(device)!!.output("Given A Given")

        //when execution completed
        listener.completed()
        //then a log message is written to output device
        Mockito.verify(device)!!.output("")

        //when execution failed
        listener.failed(RuntimeException())
        //then a log message is written to output device
        Mockito.verify(device)!!.output("Failed: java.lang.RuntimeException")
    }

    test fun on() {
        //given a text listener and a step listener that is bound to it.
        val listener = textListener.on("Spek", "A Given", "An On")

        //when execution started
        listener.started()
        //then a log message is written to output device
        Mockito.verify(device)!!.output("  On An On")

        //when execution failed
        listener.failed(RuntimeException())
        //then a log message is written to output device
        Mockito.verify(device)!!.output("  Failed: java.lang.RuntimeException")
    }

    test fun it() {
        //given a text listener and a step listener that is bound to it.
        val listener = textListener.it("Spek", "A Given", "An On", "An It")

        //when execution started
        listener.started()
        //then a log message is written to output device
        Mockito.verify(device)!!.output("    It An It")

        //when execution failed
        listener.failed(RuntimeException())
        //then a log message is written to output device
        Mockito.verify(device)!!.output("    Failed: java.lang.RuntimeException")
    }
}