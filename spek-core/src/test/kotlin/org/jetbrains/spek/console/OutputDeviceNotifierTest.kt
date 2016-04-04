package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SpekTree
import org.jetbrains.spek.api.SpekNodeRunner
import org.mockito.Mockito.*

class OutputDeviceNotifierTest : Spek({
    val device = mock(OutputDevice::class.java)
    var subject = OutputDeviceNotifier(device)

    beforeEach {
        subject = OutputDeviceNotifier(device)
    }

    afterEach {
        reset(device)
    }

    context("with a describe") {
        val describe = SpekTree("a test", ActionType.DESCRIBE, mock(SpekNodeRunner::class.java), listOf())

        describe("start") {
            beforeEach() {
                subject.start(describe)
            }

            it("prints no output") {
                verifyNoMoreInteractions(device)
            }

            it("does not increment any of the total test counts") {
                subject.finish()

                verify(device).outputLine("Found 0 tests")
                verify(device).outputLine("\u001b[32m  0 tests passed\u001b[0m")
                verify(device).outputLine("\u001b[31m  0 tests failed\u001b[0m")
                verify(device).outputLine("\u001b[33m  0 tests ignored\u001b[0m")
            }
        }

        describe("succeed") {
            beforeEach() {
                subject.succeed(describe)
            }

            it("prints no output") {
                verifyNoMoreInteractions(device)
            }

            it("does not increment any of the total test counts") {
                subject.finish()

                verify(device).outputLine("Found 0 tests")
                verify(device).outputLine("\u001b[32m  0 tests passed\u001b[0m")
                verify(device).outputLine("\u001b[31m  0 tests failed\u001b[0m")
                verify(device).outputLine("\u001b[33m  0 tests ignored\u001b[0m")
            }
        }

        describe("fail") {
            beforeEach() {
                subject.fail(describe, RuntimeException("uh oh"))
            }

            it("prints no output") {
                verifyNoMoreInteractions(device)
            }

            it("does not increment any of the total test counts") {
                subject.finish()

                verify(device).outputLine("Found 0 tests")
                verify(device).outputLine("\u001b[32m  0 tests passed\u001b[0m")
                verify(device).outputLine("\u001b[31m  0 tests failed\u001b[0m")
                verify(device).outputLine("\u001b[33m  0 tests ignored\u001b[0m")
            }
        }

        describe("ignore") {
            beforeEach() {
                subject.ignore(describe)
            }

            it("prints out a yellow dot") {
                org.mockito.Mockito.verify(device).output("[33m.[0m")
            }

            it("increments the number of ignored tests") {
                subject.finish()

                org.mockito.Mockito.verify(device, org.mockito.Mockito.times(3)).outputLine("")
                org.mockito.Mockito.verify(device).outputLine("[33mIgnored pending test: a test[0m")
                org.mockito.Mockito.verify(device).outputLine("Found 1 tests")
                org.mockito.Mockito.verify(device).outputLine("\u001b[32m  0 tests passed\u001b[0m")
                org.mockito.Mockito.verify(device).outputLine("\u001b[31m  0 tests failed\u001b[0m")
                org.mockito.Mockito.verify(device).outputLine("\u001b[33m  1 tests ignored\u001b[0m")
            }
        }
    }

    context("with an it") {
        val it = SpekTree("another test", ActionType.IT, mock(SpekNodeRunner::class.java), listOf())

        describe("start") {
            beforeEach() {
                subject.start(it)
            }

            it("prints no output") {
                verifyNoMoreInteractions(device)
            }

            it("does not increment any of the total test counts") {
                subject.finish()

                verify(device).outputLine("Found 0 tests")
                verify(device).outputLine("\u001b[32m  0 tests passed\u001b[0m")
                verify(device).outputLine("\u001b[31m  0 tests failed\u001b[0m")
                verify(device).outputLine("\u001b[33m  0 tests ignored\u001b[0m")
            }
        }

        describe("succeed") {
            beforeEach() {
                subject.succeed(it)
            }

            it("prints a green dot") {
                verify(device).output("[32m.[0m")
            }

            it("does not increments the number of passed tests") {
                subject.finish()

                verify(device).outputLine("Found 1 tests")
                verify(device).outputLine("\u001b[32m  1 tests passed\u001b[0m")
                verify(device).outputLine("\u001b[31m  0 tests failed\u001b[0m")
                verify(device).outputLine("\u001b[33m  0 tests ignored\u001b[0m")
            }
        }

        describe("fail") {
            beforeEach() {
                subject.fail(it, RuntimeException("fail whale"))
            }

            it("prints a red dot") {
                verify(device).output("[31m.[0m")
            }

            it("increments the number of failed tests") {
                subject.finish()

                verify(device).outputLine("[31mFailed: fail whale java.lang.RuntimeException: fail whale[0m")
                verify(device).outputLine("Found 1 tests")
                verify(device).outputLine("\u001b[32m  0 tests passed\u001b[0m")
                verify(device).outputLine("\u001b[31m  1 tests failed\u001b[0m")
                verify(device).outputLine("\u001b[33m  0 tests ignored\u001b[0m")
            }
        }

        describe("ignore") {
            beforeEach() {
                subject.ignore(it)
            }

            it("prints out a yellow dot") {
                verify(device).output("[33m.[0m")
            }

            it("increments the number of ignored tests") {
                subject.finish()

                verify(device, times(3)).outputLine("")
                verify(device).outputLine("[33mIgnored pending test: another test[0m")
                verify(device).outputLine("Found 1 tests")
                verify(device).outputLine("\u001b[32m  0 tests passed\u001b[0m")
                verify(device).outputLine("\u001b[31m  0 tests failed\u001b[0m")
                verify(device).outputLine("\u001b[33m  1 tests ignored\u001b[0m")
            }
        }

    }
})