package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SpekTree
import org.jetbrains.spek.api.SpekNodeRunner
import org.mockito.Mockito.*
import kotlin.test.assertEquals

class OutputDeviceVerboseNotifierTest : Spek({
    val device = mock(OutputDevice::class.java)
    var subject = OutputDeviceVerboseNotifier(device)

    beforeEach {
        subject = OutputDeviceVerboseNotifier(device)
    }

    afterEach {
        reset(device)
    }

    it("prints out text for starting a test") {
        var spekTree = SpekTree("a test", ActionType.DESCRIBE, mock(SpekNodeRunner::class.java), listOf())
        subject.start(spekTree)

        verify(device).outputLine("a test")

        spekTree = SpekTree("another test", ActionType.DESCRIBE, mock(SpekNodeRunner::class.java), listOf())
        subject.start(spekTree)

        verify(device).outputLine("  another test")

        assertEquals(2, subject.indentation, "notifier indentation level")
    }

    it("prints decreases the indentation level when passing a test and increments the passed tests") {
        var spekTree = SpekTree("a test", ActionType.DESCRIBE, mock(SpekNodeRunner::class.java), listOf())
        subject.indentation = 2
        subject.succeed(spekTree)

        spekTree = SpekTree("a test", ActionType.IT, mock(SpekNodeRunner::class.java), listOf())
        subject.succeed(spekTree)

        assertEquals(0, subject.indentation, "notifier indentation level")
        assertEquals(1, subject.testsPassed, "Tests passed")
    }

    it("prints out an error message when failing a test and increments the failed tests") {
        subject.indentation = 1
        val spekTree = SpekTree("a test", ActionType.IT, mock(SpekNodeRunner::class.java), listOf())

        subject.fail(spekTree, RuntimeException("test error"))

        verify(device, times(2))!!.outputLine("")
        verify(device).outputLine("  \u001B[31mFailed: test error java.lang.RuntimeException: test error\u001B[0m")

        assertEquals(0, subject.indentation, "notifier indentation level")
        assertEquals(1, subject.testsFailed, "Tests failed")
    }

    it("prints out a messages for ignoring a test and increments ignored tests") {
        val spekTree = SpekTree("an ignore", ActionType.IT, mock(SpekNodeRunner::class.java), listOf())
        subject.ignore(spekTree)

        verify(device).outputLine("\u001B[33mIgnored pending test: an ignore\u001b[0m")
        assertEquals(1, subject.testsIgnored, "Tests ignored")
    }

    it("prints out a summary message when finishing the test suite") {
        subject.finish()

        verify(device).outputLine("")
        verify(device).outputLine("Found 0 tests")
        verify(device).outputLine("\u001b[32m  0 tests passed\u001b[0m")
        verify(device).outputLine("\u001b[31m  0 tests failed\u001b[0m")
        verify(device).outputLine("\u001b[33m  0 tests ignored\u001b[0m")

        val error = RuntimeException("test error")
        val spekTree = SpekTree("a test", ActionType.IT, mock(SpekNodeRunner::class.java), listOf())
        subject.indentation = 2

        subject.fail(spekTree, error)
        subject.succeed(spekTree)
        subject.ignore(spekTree)

        reset(device)

        subject.finish()

        verify(device).outputLine("")
        verify(device).outputLine("Found 3 tests")
        verify(device).outputLine("\u001b[32m  1 tests passed\u001b[0m")
        verify(device).outputLine("\u001b[31m  1 tests failed\u001b[0m")
        verify(device).outputLine("\u001b[33m  1 tests ignored\u001b[0m")
    }
})