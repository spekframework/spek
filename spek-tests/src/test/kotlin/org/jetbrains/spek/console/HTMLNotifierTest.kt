package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.SpekTree
import org.jetbrains.spek.api.SpekTreeRunner
import org.mockito.Mockito
import kotlin.test.assertEquals
import org.junit.Before as before
import org.junit.Test as test

class HTMLNotifierTest {
    val device = Mockito.mock(OutputDevice::class.java)!!
    val subject = HtmlNotifier("", device)

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
        val spekTree = SpekTree("a test", ActionType.DESCRIBE, Mockito.mock(SpekTreeRunner::class.java), listOf())
        subject.start(spekTree)

        Mockito.verify(device)!!.output("<li>a test")
        Mockito.verify(device)!!.output("<ul>")
    }

    @test fun startIt() {
        val spekTree = SpekTree("a test", ActionType.IT, Mockito.mock(SpekTreeRunner::class.java), listOf())

        subject.start(spekTree)

        Mockito.verify(device)!!.output("<li>a test:")
    }

    @test fun succeedDescribe() {
        val spekTree = SpekTree("a test", ActionType.DESCRIBE, Mockito.mock(SpekTreeRunner::class.java), listOf())
        subject.succeed(spekTree)
        Mockito.verify(device)!!.output("</ul>")
        Mockito.verify(device)!!.output("</li>")
        assertEquals(0, subject.testsPassed, "Didn't change passed test count")
    }

    @test fun succeedIt() {
        val spekTree = SpekTree("a test", ActionType.IT, Mockito.mock(SpekTreeRunner::class.java), listOf())
        subject.succeed(spekTree)
        Mockito.verify(device)!!.output("<span style=\"color: #2C2;\">Passed</span>")
        Mockito.verify(device)!!.output("</li>")
        assertEquals(1, subject.testsPassed, "registered the passed test")
    }

    @test fun fail() {
        val spekTree = SpekTree("a test", ActionType.DESCRIBE, Mockito.mock(SpekTreeRunner::class.java), listOf())
        subject.fail(spekTree, RuntimeException("uh oh"))

        Mockito.verify(device)!!.output("<p style=\"color: red;\">Failed: java.lang.RuntimeException: uh oh</p>")
        Mockito.verify(device)!!.output("</li>")
        assertEquals(1, subject.testsFailed, "registered the failed test")
    }

    @test fun ignore() {
        val spekTree = SpekTree("ignored test", ActionType.DESCRIBE, Mockito.mock(SpekTreeRunner::class.java), listOf())
        subject.ignore(spekTree)

        Mockito.verify(device)!!.output("<li><span style=\"color: darkgoldenrod;\">Ignored pending test: ignored test</span>")
        Mockito.verify(device)!!.output("</li>")
        assertEquals(1, subject.testsIgnored, "registered the ignored test")
    }

    @test fun finish() {
        subject.finish()

        Mockito.verify(device, Mockito.times(2))!!.output("</ul>")
        Mockito.verify(device)!!.output("<h2>Summary: 0 tests found</h2>")
        Mockito.verify(device)!!.output("<ul>")
        Mockito.verify(device)!!.output("<li><span style=\"color: #2C2;\">0 tests passed</span></li>")
        Mockito.verify(device)!!.output("<li><span style=\"color: red;\">0 tests failed</span></li>")
        Mockito.verify(device)!!.output("<li><span style=\"color: darkgoldenrod;\">0 tests ignored</span></li>")
        Mockito.verify(device)!!.output("</body></html>")
    }
}