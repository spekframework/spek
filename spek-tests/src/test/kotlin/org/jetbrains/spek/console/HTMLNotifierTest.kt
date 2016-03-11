package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.TestAction
import org.mockito.Mockito
import kotlin.test.assertEquals
import org.junit.Before as before
import org.junit.Test as test

class HTMLNotifierTest {
    val device = Mockito.mock(OutputDevice::class.java)!!
    val htmlNotifier = HtmlNotifier("", device)
    val testAction = Mockito.mock(TestAction::class.java)

    @before fun clearDevice() {
        Mockito.reset(device)
    }

    @test fun initializing() {
        HtmlNotifier("another test suite", device)
        val expectedText = "<html><head><title>another test suite</title></head><body><h2>another test suite</h2>"
        Mockito.verify(device)!!.output(expectedText)
        Mockito.verify(device)!!.output("<ul>")
    }

    @test fun startDescribe() {
        Mockito.`when`(testAction.description()).thenReturn("a test")
        Mockito.`when`(testAction.type()).thenReturn(ActionType.DESCRIBE)
        htmlNotifier.start(testAction)

        Mockito.verify(device)!!.output("<li>a test")
        Mockito.verify(device)!!.output("<ul>")
    }

    @test fun startIt() {
        Mockito.`when`(testAction.description()).thenReturn("a test")
        Mockito.`when`(testAction.type()).thenReturn(ActionType.IT)

        htmlNotifier.start(testAction)

        Mockito.verify(device)!!.output("<li>a test:")
    }

    @test fun succeedDescribe() {
        Mockito.`when`(testAction.type()).thenReturn(ActionType.DESCRIBE)
        htmlNotifier.succeed(testAction)
        Mockito.verify(device)!!.output("</ul>")
        Mockito.verify(device)!!.output("</li>")
        assertEquals(0, htmlNotifier.testsPassed, "Didn't change passed test count")
    }

    @test fun succeedIt() {
        Mockito.`when`(testAction.type()).thenReturn(ActionType.IT)
        htmlNotifier.succeed(testAction)
        Mockito.verify(device)!!.output("<span style=\"color: #2C2;\">Passed</span>")
        Mockito.verify(device)!!.output("</li>")
        assertEquals(1, htmlNotifier.testsPassed, "registered the passed test")
    }

    @test fun fail() {
        htmlNotifier.fail(testAction, RuntimeException("uh oh"))

        Mockito.verify(device)!!.output("<p style=\"color: red;\">Failed: java.lang.RuntimeException: uh oh</p>")
        Mockito.verify(device)!!.output("</li>")
        assertEquals(1, htmlNotifier.testsFailed, "registered the failed test")
    }

    @test fun ignore() {
        Mockito.`when`(testAction.description()).thenReturn("ignored test")
        htmlNotifier.ignore(testAction)

        Mockito.verify(device)!!.output("<li><span style=\"color: darkgoldenrod;\">Ignored pending test: ignored test</span>")
        Mockito.verify(device)!!.output("</li>")
        assertEquals(1, htmlNotifier.testsIgnored, "registered the ignored test")
    }

    @test fun finish() {
        htmlNotifier.finish()

        Mockito.verify(device, Mockito.times(2))!!.output("</ul>")
        Mockito.verify(device)!!.output("<h2>Summary: 0 tests found</h2>")
        Mockito.verify(device)!!.output("<ul>")
        Mockito.verify(device)!!.output("<li><span style=\"color: #2C2;\">0 tests passed</span></li>")
        Mockito.verify(device)!!.output("<li><span style=\"color: red;\">0 tests failed</span></li>")
        Mockito.verify(device)!!.output("<li><span style=\"color: darkgoldenrod;\">0 tests ignored</span></li>")
        Mockito.verify(device)!!.output("</body></html>")
    }
}