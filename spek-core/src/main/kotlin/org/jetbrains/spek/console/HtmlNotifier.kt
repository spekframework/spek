package org.jetbrains.spek.console

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.SpekTree

class HtmlNotifier(val suite: String, val device: OutputDevice) : ConsoleNotifier {
    var testsPassed = 0
    var testsFailed = 0
    var testsIgnored = 0

    val passStyle = "style=\"color: #2C2;\""
    val failStyle = "style=\"color: red;\""
    val ignoreStyle = "style=\"color: darkgoldenrod;\""

    init {
        device.outputLine("<html><head><title>$suite</title></head><body><h2>$suite</h2>")
        device.outputLine("<ul>")
    }

    override fun start(key: SpekTree) {
        when (key.type) {
            ActionType.DESCRIBE -> {
                device.outputLine("<li>${key.description}")
                device.outputLine("<ul>")
            }
            ActionType.IT ->
                device.outputLine("<li>${key.description}:")
        }
    }

    override fun succeed(key: SpekTree) {
        when (key.type) {
            ActionType.DESCRIBE ->
                device.outputLine("</ul>")
            ActionType.IT -> {
                device.outputLine("<span ${passStyle}>Passed</span>")
                testsPassed++
            }
        }
        device.outputLine("</li>")
    }

    override fun fail(key: SpekTree, error: Throwable) {
        device.outputLine("<p ${failStyle}>Failed: ${error}</p>")
        device.outputLine("</li>")
        testsFailed++
    }

    override fun ignore(key: SpekTree) {
        device.outputLine("<li><span ${ignoreStyle}>Ignored pending test: ${key.description}</span>")
        device.outputLine("</li>")
        testsIgnored++
    }

    override fun finish() {
        device.outputLine("</ul>")
        device.outputLine("<h2>Summary: ${testsPassed + testsFailed + testsIgnored} tests found</h2>")
        device.outputLine("<ul>")
        device.outputLine("<li><span ${passStyle}>${testsPassed} tests passed</span></li>")
        device.outputLine("<li><span ${failStyle}>${testsFailed} tests failed</span></li>")
        device.outputLine("<li><span ${ignoreStyle}>${testsIgnored} tests ignored</span></li>")
        device.outputLine("</ul>")
        device.outputLine("</body></html>")
    }
}

