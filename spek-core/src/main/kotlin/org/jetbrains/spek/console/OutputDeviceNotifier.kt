package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.SpekTree

class OutputDeviceNotifier(val device: OutputDevice) : ConsoleNotifier {
    var testsPassed = 0
    var testsFailed = 0
    var testsIgnored = 0

    val currentMessages: MutableList<String> = mutableListOf()
    val finalMessages: MutableList<String> = mutableListOf()

    override fun start(key: SpekTree) {
        currentMessages.add(key.description)
    }

    override fun succeed(key: SpekTree) {
        if (key.type == ActionType.IT) {
            device.output(greenText("."))
            testsPassed++
        }
        currentMessages.remove(key.description)
    }

    override fun fail(key: SpekTree, error: Throwable) {
        if (key.type == ActionType.IT) {
            device.output(redText("."))
            testsFailed++
        }
        currentMessages.add(redText("Failed: " + error.message + " " + error))
        flushMessageBuffer(currentMessages)
    }

    override fun ignore(key: SpekTree) {
        device.output(yellowText("."))
        testsIgnored++
        currentMessages.add(yellowText("Ignored pending test: ${key.description}"))
        flushMessageBuffer(currentMessages)
    }

    override fun finish() {
        device.outputLine("")
        finalMessages.forEach { device.outputLine(it) }
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

    private fun flushMessageBuffer(messages: MutableList<String>) {
        var indentation = 0
        finalMessages.add("")
        messages.forEach {
            finalMessages.add(lineWithIndentation(it, indentation))
            indentation++
        }
        messages.clear()
    }

    private fun lineWithIndentation(text: String, indentation: Int) : String {
        return "  ".repeat(indentation) + text
    }
}
