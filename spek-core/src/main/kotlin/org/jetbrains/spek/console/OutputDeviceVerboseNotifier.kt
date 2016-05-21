package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.SpekTree

class OutputDeviceVerboseNotifier(val device: OutputDevice) : ConsoleNotifier {
    var indentation = 0
    var testsPassed = 0
    var testsFailed = 0
    var testsIgnored = 0

    override fun start(key: SpekTree) {
        printWithIndentation(key.description)
        indentation++
    }

    override fun succeed(key: SpekTree) {
        if (key.type == ActionType.IT) {
            testsPassed++
        }
        indentation--
    }

    override fun fail(key: SpekTree, error: Throwable) {
        if (key.type == ActionType.IT) {
            testsFailed++
        }
        device.outputLine("")
        printWithIndentation(redText("Failed: " + error.message + " " + error))
        device.outputLine("")
        indentation--
    }

    override fun ignore(key: SpekTree) {
        if (key.type == ActionType.IT) {
            testsIgnored++
        }
        printWithIndentation(yellowText("Ignored pending test: ${key.description}"))
    }

    override fun finish() {
        device.outputLine("")
        device.outputLine("Found ${testsPassed + testsFailed + testsIgnored} tests")
        device.outputLine(greenText("  ${testsPassed} tests passed"))
        device.outputLine(redText("  ${testsFailed} tests failed"))
        device.outputLine(yellowText("  ${testsIgnored} tests ignored"))
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
        device.outputLine(getIndentationString() + text)
    }

    private fun getIndentationString(): String {
        return "  ".repeat(indentation)
    }
}
