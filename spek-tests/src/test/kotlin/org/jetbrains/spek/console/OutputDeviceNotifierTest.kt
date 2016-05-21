package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.TestAction
import org.mockito.Mockito
import kotlin.test.assertEquals
import org.junit.Test as test

class OutputDeviceNotifierTest {
    val device = Mockito.mock(OutputDevice::class.java)!!
    val textNotifier = OutputDeviceNotifier(device)
    val testAction = Mockito.mock(TestAction::class.java)

    @test fun start() {
        Mockito.`when`(testAction.description()).thenReturn("a test")
        textNotifier.start(testAction)

        Mockito.verify(device)!!.output("a test")

        Mockito.`when`(testAction.description()).thenReturn("another test")
        textNotifier.start(testAction)

        Mockito.verify(device)!!.output("  another test")

        assertEquals(2, textNotifier.indentation, "notifier indentation level")
    }

    @test fun succeed() {
        textNotifier.indentation = 2
        textNotifier.succeed(testAction)
        Mockito.`when`(testAction.type()).thenReturn(ActionType.IT)
        textNotifier.succeed(testAction)
        assertEquals(0, textNotifier.indentation, "notifier indentation level")

        assertEquals(1, textNotifier.testsPassed, "Tests passed")
    }

    @test fun fail() {
        textNotifier.indentation = 1
        Mockito.`when`(testAction.type()).thenReturn(ActionType.IT)

        textNotifier.fail(testAction, RuntimeException("test error"))

        Mockito.verify(device, Mockito.times(2))!!.output("")
        Mockito.verify(device)!!.output("  \u001B[31mFailed: test error java.lang.RuntimeException: test error\u001B[0m")

        assertEquals(0, textNotifier.indentation, "notifier indentation level")
        assertEquals(1, textNotifier.testsFailed, "Tests failed")
    }

    @test fun ignore() {
        Mockito.`when`(testAction.description()).thenReturn("an ignore")
        Mockito.`when`(testAction.type()).thenReturn(ActionType.IT)
        textNotifier.ignore(testAction)

        Mockito.verify(device)!!.output("\u001B[33mIgnored pending test: an ignore\u001b[0m")
        assertEquals(1, textNotifier.testsIgnored, "Tests ignored")
    }

    @test fun finish() {
        textNotifier.finish()

        Mockito.verify(device)!!.output("")
        Mockito.verify(device)!!.output("Found 0 tests")
        Mockito.verify(device)!!.output("\u001b[32m  0 tests passed\u001b[0m")
        Mockito.verify(device)!!.output("\u001b[31m  0 tests failed\u001b[0m")
        Mockito.verify(device)!!.output("\u001b[33m  0 tests ignored\u001b[0m")

        val error = RuntimeException("test error")
        Mockito.`when`(testAction.type()).thenReturn(ActionType.IT)
        textNotifier.indentation = 2

        textNotifier.fail(testAction, error)
        textNotifier.succeed(testAction)
        textNotifier.ignore(testAction)

        Mockito.reset(device)

        textNotifier.finish()

        Mockito.verify(device)!!.output("")
        Mockito.verify(device)!!.output("Found 3 tests")
        Mockito.verify(device)!!.output("\u001b[32m  1 tests passed\u001b[0m")
        Mockito.verify(device)!!.output("\u001b[31m  1 tests failed\u001b[0m")
        Mockito.verify(device)!!.output("\u001b[33m  1 tests ignored\u001b[0m")
    }
}