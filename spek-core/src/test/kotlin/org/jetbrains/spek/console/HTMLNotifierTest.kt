package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SpekTree
import org.jetbrains.spek.api.SpekNodeRunner
import org.mockito.Mockito.*
import kotlin.test.assertEquals

class HTMLNotifierTest : Spek({
    val device = mock(OutputDevice::class.java)
    var subject = HtmlNotifier("a test suite", device)

    beforeEach {
        subject = HtmlNotifier("a test suite", device)
        reset(device)
    }

    it("prints out initialization text") {
        subject = HtmlNotifier("a test suite", device)
        val expectedText = "<html><head><title>a test suite</title></head><body><h2>a test suite</h2>"
        verify(device).outputLine(expectedText)
        verify(device).outputLine("<ul>")
    }

    context("with a describe") {
        val spekTree = SpekTree("a test", ActionType.DESCRIBE, mock(SpekNodeRunner::class.java), listOf())

        it("creates <ul> nested in a <li> on start")  {
            subject.start(spekTree)

            verify(device).outputLine("<li>a test")
            verify(device).outputLine("<ul>")
        }

        it("closes the <ul> and <li> on succeed") {
            subject.succeed(spekTree)
            verify(device).outputLine("</ul>")
            verify(device).outputLine("</li>")
        }

        it("does not change the passed test count on succeed") {
            subject.succeed(spekTree)
            assertEquals(0, subject.testsPassed)
        }
    }

    context("with an it") {
        val spekTree = SpekTree("a test", ActionType.IT, mock(SpekNodeRunner::class.java), listOf())

        it("creates a <li> on start") {
            subject.start(spekTree)
            verify(device).outputLine("<li>a test:")
        }

        it("closes the <li> on succeed") {
            subject.succeed(spekTree)
            verify(device).outputLine("<span style=\"color: #2C2;\">Passed</span>")
            verify(device).outputLine("</li>")
        }

        it("increments the passed test count on succeed") {
            subject.succeed(spekTree)
            assertEquals(1, subject.testsPassed, "registered the passed test")
        }

        it("prints an error message on failure") {
            subject.fail(spekTree, RuntimeException("uh oh"))

            verify(device).outputLine("<p style=\"color: red;\">Failed: java.lang.RuntimeException: uh oh</p>")
            verify(device).outputLine("</li>")
        }

        it("increments the failed test count on failure") {
            subject.fail(spekTree, RuntimeException("uh oh"))
            assertEquals(1, subject.testsFailed, "registered the failed test")
        }

        it("prints a message for ignored tests") {
            subject.ignore(spekTree)
            verify(device).outputLine("<li><span style=\"color: darkgoldenrod;\">Ignored pending test: a test</span>")
            verify(device).outputLine("</li>")
        }

        it("increments the ignored test count on ignore") {
            subject.ignore(spekTree)
            assertEquals(1, subject.testsIgnored, "registered the ignored test")
        }
    }

    it("prints out summary text on finish") {
        subject.finish()

        verify(device, times(2))!!.outputLine("</ul>")
        verify(device).outputLine("<h2>Summary: 0 tests found</h2>")
        verify(device).outputLine("<ul>")
        verify(device).outputLine("<li><span style=\"color: #2C2;\">0 tests passed</span></li>")
        verify(device).outputLine("<li><span style=\"color: red;\">0 tests failed</span></li>")
        verify(device).outputLine("<li><span style=\"color: darkgoldenrod;\">0 tests ignored</span></li>")
        verify(device).outputLine("</body></html>")
    }
})