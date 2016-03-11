package org.jetbrains.spek.console

import org.jetbrains.spek.api.TestAction
import org.mockito.Mockito
import org.junit.Before as before
import org.junit.Test as test

class HTMLNotifierTest {
    val device = Mockito.mock(OutputDevice::class.java)!!
    val htmlNotifier = HtmlNotifier("", device, "")
    val testAction = Mockito.mock(TestAction::class.java)

    @before fun clearDevice() {
        Mockito.reset(device)
    }

    @test fun initializing() {
        HtmlNotifier("test suite title", device, "myCss.css")
        val expectedCSSText = "<html><head><title>test suite title</title><link rel=\"stylesheet\" type=\"text/css\" href=\"myCss.css\"></head><body><div class=\"suite\">"
        Mockito.verify(device)!!.output(expectedCSSText)

        Mockito.reset(device)

        HtmlNotifier("another test suite", device, "")
        val expectedText = "<html><head><title>another test suite</title></head><body><div class=\"suite\">"
        Mockito.verify(device)!!.output(expectedText)
    }

    @test fun start() {
        Mockito.`when`(testAction.description()).thenReturn("a test")
        htmlNotifier.start(testAction)

        Mockito.verify(device)!!.output("<div class=\"spek\"><h1>a test</h1>")
    }

    @test fun succeed() {
        htmlNotifier.succeed(testAction)

        Mockito.verify(device)!!.output("</div>")
    }

    @test fun fail() {
        htmlNotifier.fail(testAction, RuntimeException("uh oh"))

        Mockito.verify(device)!!.output("Failed: java.lang.RuntimeException: uh oh")
        Mockito.verify(device)!!.output("")
        Mockito.verify(device)!!.output("</div>")
    }

    @test fun ignore() {
        Mockito.`when`(testAction.description()).thenReturn("ignored test")
        htmlNotifier.ignore(testAction)

        Mockito.verify(device)!!.output("<div class=\"spek\"><h1>ignored test</h1>")
        Mockito.verify(device)!!.output("Ignored pending test")
        Mockito.verify(device)!!.output("</div>")
    }

    @test fun finish() {
        htmlNotifier.finish()

        Mockito.verify(device)!!.output("</div></body></html>")
    }
}