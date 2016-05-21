package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.TestAction

class OutputDeviceNotifier(val device: OutputDevice) : ConsoleNotifier {
    var indentation = 0
    var testsPassed = 0
    var testsFailed = 0
    var testsIgnored = 0

    override fun start(key: TestAction) {
        printWithIndentation(key.description())
        indentation++
    }

    override fun succeed(key: TestAction) {
        if (key.type() == ActionType.IT) {
            testsPassed++
        }
        indentation--
    }

    override fun fail(key: TestAction, error: Throwable) {
        if (key.type() == ActionType.IT) {
            testsFailed++
        }
        device.output("")
        printWithIndentation(redText("Failed: " + error.message + " " + error))
        device.output("")
        indentation--
    }

    override fun ignore(key: TestAction) {
        if (key.type() == ActionType.IT) {
            testsIgnored++
        }
        printWithIndentation(yellowText("Ignored pending test: ${key.description()}"))
    }

    override fun finish() {
        device.output("")
        device.output("Found ${testsPassed + testsFailed + testsIgnored} tests")
        device.output(greenText("  ${testsPassed} tests passed"))
        device.output(redText("  ${testsFailed} tests failed"))
        device.output(yellowText("  ${testsIgnored} tests ignored"))
    }

    private fun redText(text: String): String {
        return "\u001B[31m${text}\u001B[0m"
    }

    private fun yellowText(text: String): String {
        return "\u001B[33m${text}\u001B[0m"
    }

    private fun greenText(text: String): String {
        return "\u001B[32m${text}\u001B[0m"
    }

    private fun printWithIndentation(text: String) {
        device.output(getIndentationString() + text)
    }

    private fun getIndentationString(): String {
        return "  ".repeat(indentation)
    }
}
