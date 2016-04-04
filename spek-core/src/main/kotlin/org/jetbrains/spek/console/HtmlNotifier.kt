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
        device.output("<html><head><title>$suite</title></head><body><h2>$suite</h2>")
        device.output("<ul>")
    }

    override fun start(key: SpekTree) {
        when (key.type) {
            ActionType.DESCRIBE -> {
                device.output("<li>${key.description}")
                device.output("<ul>")
            }
            ActionType.IT ->
                device.output("<li>${key.description}:")
        }
    }

    override fun succeed(key: SpekTree) {
        when (key.type) {
            ActionType.DESCRIBE ->
                device.output("</ul>")
            ActionType.IT -> {
                device.output("<span ${passStyle}>Passed</span>")
                testsPassed++
            }
        }
        device.output("</li>")
    }

    override fun fail(key: SpekTree, error: Throwable) {
        device.output("<p ${failStyle}>Failed: ${error}</p>")
        device.output("</li>")
        testsFailed++
    }

    override fun ignore(key: SpekTree) {
        device.output("<li><span ${ignoreStyle}>Ignored pending test: ${key.description}</span>")
        device.output("</li>")
        testsIgnored++
    }

    override fun finish() {
        device.output("</ul>")
        device.output("<h2>Summary: ${testsPassed + testsFailed + testsIgnored} tests found</h2>")
        device.output("<ul>")
        device.output("<li><span ${passStyle}>${testsPassed} tests passed</span></li>")
        device.output("<li><span ${failStyle}>${testsFailed} tests failed</span></li>")
        device.output("<li><span ${ignoreStyle}>${testsIgnored} tests ignored</span></li>")
        device.output("</ul>")
        device.output("</body></html>")
    }
}

