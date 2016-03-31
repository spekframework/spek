package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.SpekTree
import org.jetbrains.spek.api.SpekNodeRunner
import org.mockito.Mockito
import kotlin.test.assertEquals
import org.junit.Test as test

class OutputDeviceNotifierTest {
    val device = Mockito.mock(OutputDevice::class.java)!!
    val subject = OutputDeviceVerboseNotifier(device)

    @test fun start() {
        var spekTree = SpekTree("a test", ActionType.DESCRIBE, Mockito.mock(SpekNodeRunner::class.java), listOf())
        subject.start(spekTree)

        Mockito.verify(device).output("a test")

        spekTree = SpekTree("another test", ActionType.DESCRIBE, Mockito.mock(SpekNodeRunner::class.java), listOf())
        subject.start(spekTree)

        Mockito.verify(device)!!.output("  another test")

        assertEquals(2, subject.indentation, "notifier indentation level")
    }

    @test fun succeed() {
        var spekTree = SpekTree("a test", ActionType.DESCRIBE, Mockito.mock(SpekNodeRunner::class.java), listOf())
        subject.indentation = 2
        subject.succeed(spekTree)

        spekTree = SpekTree("a test", ActionType.IT, Mockito.mock(SpekNodeRunner::class.java), listOf())
        subject.succeed(spekTree)

        assertEquals(0, subject.indentation, "notifier indentation level")
        assertEquals(1, subject.testsPassed, "Tests passed")
    }

    @test fun fail() {
        subject.indentation = 1
        val spekTree = SpekTree("a test", ActionType.IT, Mockito.mock(SpekNodeRunner::class.java), listOf())

        subject.fail(spekTree, RuntimeException("test error"))

        Mockito.verify(device, Mockito.times(2))!!.output("")
        Mockito.verify(device)!!.output("  \u001B[31mFailed: test error java.lang.RuntimeException: test error\u001B[0m")

        assertEquals(0, subject.indentation, "notifier indentation level")
        assertEquals(1, subject.testsFailed, "Tests failed")
    }

    @test fun ignore() {
        val spekTree = SpekTree("an ignore", ActionType.IT, Mockito.mock(SpekNodeRunner::class.java), listOf())
        subject.ignore(spekTree)

        Mockito.verify(device)!!.output("\u001B[33mIgnored pending test: an ignore\u001b[0m")
        assertEquals(1, subject.testsIgnored, "Tests ignored")
    }

    @test fun finish() {
        subject.finish()

        Mockito.verify(device)!!.output("")
        Mockito.verify(device)!!.output("Found 0 tests")
        Mockito.verify(device)!!.output("\u001b[32m  0 tests passed\u001b[0m")
        Mockito.verify(device)!!.output("\u001b[31m  0 tests failed\u001b[0m")
        Mockito.verify(device)!!.output("\u001b[33m  0 tests ignored\u001b[0m")

        val error = RuntimeException("test error")
        val spekTree = SpekTree("a test", ActionType.IT, Mockito.mock(SpekNodeRunner::class.java), listOf())
        subject.indentation = 2

        subject.fail(spekTree, error)
        subject.succeed(spekTree)
        subject.ignore(spekTree)

        Mockito.reset(device)

        subject.finish()

        Mockito.verify(device)!!.output("")
        Mockito.verify(device)!!.output("Found 3 tests")
        Mockito.verify(device)!!.output("\u001b[32m  1 tests passed\u001b[0m")
        Mockito.verify(device)!!.output("\u001b[31m  1 tests failed\u001b[0m")
        Mockito.verify(device)!!.output("\u001b[33m  1 tests ignored\u001b[0m")
    }
}