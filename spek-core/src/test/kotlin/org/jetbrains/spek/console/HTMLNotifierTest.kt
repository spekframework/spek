package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.SpekTree
import org.jetbrains.spek.api.SpekNodeRunner
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import org.junit.Before as before
import org.junit.Test as test

class HTMLNotifierTest {
    val device = mock(OutputDevice::class.java)!!
    val subject = HtmlNotifier("", device)

    @before fun clearDevice() {
        reset(device)
    }

    @test fun initializing() {
        HtmlNotifier("another test suite", device)
        val expectedText = "<html><head><title>another test suite</title></head><body><h2>another test suite</h2>"
        verify(device)!!.output(expectedText)
        verify(device)!!.output("<ul>")
    }

    @test fun startDescribe() {
        val spekTree = SpekTree("a test", ActionType.DESCRIBE, mock(SpekNodeRunner::class.java), listOf())
        subject.start(spekTree)

        verify(device)!!.output("<li>a test")
        verify(device)!!.output("<ul>")
    }

    @test fun startIt() {
        val spekTree = SpekTree("a test", ActionType.IT, mock(SpekNodeRunner::class.java), listOf())

        subject.start(spekTree)

        verify(device)!!.output("<li>a test:")
    }

    @test fun succeedDescribe() {
        val spekTree = SpekTree("a test", ActionType.DESCRIBE, mock(SpekNodeRunner::class.java), listOf())
        subject.succeed(spekTree)
        verify(device)!!.output("</ul>")
        verify(device)!!.output("</li>")
        assertEquals(0, subject.testsPassed, "Didn't change passed test count")
    }

    @test fun succeedIt() {
        val spekTree = SpekTree("a test", ActionType.IT, mock(SpekNodeRunner::class.java), listOf())
        subject.succeed(spekTree)
        verify(device)!!.output("<span style=\"color: #2C2;\">Passed</span>")
        verify(device)!!.output("</li>")
        assertEquals(1, subject.testsPassed, "registered the passed test")
    }

    @test fun fail() {
        val spekTree = SpekTree("a test", ActionType.DESCRIBE, mock(SpekNodeRunner::class.java), listOf())
        subject.fail(spekTree, RuntimeException("uh oh"))

        verify(device)!!.output("<p style=\"color: red;\">Failed: java.lang.RuntimeException: uh oh</p>")
        verify(device)!!.output("</li>")
        assertEquals(1, subject.testsFailed, "registered the failed test")
    }

    @test fun ignore() {
        val spekTree = SpekTree("ignored test", ActionType.DESCRIBE, mock(SpekNodeRunner::class.java), listOf())
        subject.ignore(spekTree)

        verify(device)!!.output("<li><span style=\"color: darkgoldenrod;\">Ignored pending test: ignored test</span>")
        verify(device)!!.output("</li>")
        assertEquals(1, subject.testsIgnored, "registered the ignored test")
    }

    @test fun finish() {
        subject.finish()

        verify(device, times(2))!!.output("</ul>")
        verify(device)!!.output("<h2>Summary: 0 tests found</h2>")
        verify(device)!!.output("<ul>")
        verify(device)!!.output("<li><span style=\"color: #2C2;\">0 tests passed</span></li>")
        verify(device)!!.output("<li><span style=\"color: red;\">0 tests failed</span></li>")
        verify(device)!!.output("<li><span style=\"color: darkgoldenrod;\">0 tests ignored</span></li>")
        verify(device)!!.output("</body></html>")
    }
}