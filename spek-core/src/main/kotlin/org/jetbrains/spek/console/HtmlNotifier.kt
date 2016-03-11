package org.jetbrains.spek.console

import org.jetbrains.spek.api.TestAction

class HtmlNotifier(val suite: String, val device: OutputDevice, val cssFile: String) : ConsoleNotifier {
    var css = ""

    init {
        if (cssFile != "") {
            css = "<link rel=\"stylesheet\" type=\"text/css\" href=\"$cssFile\">"
        }
        device.output("<html><head><title>$suite</title>$css<body><div class=\"suite\">")
    }

    override fun start(key: TestAction) {
        device.output("<div class=\"spek\"><h1>${key.description()}</h1>")
    }

    override fun succeed(key: TestAction) {
        device.output("</div>")
    }

    override fun fail(key: TestAction, error: Throwable) {
        device.output("Failed: ${error}")
        device.output("")
        device.output("</div>")
    }

    override fun ignore(key: TestAction) {
        device.output("<div class=\"spek\"><h1>${key.description()}</h1>")
        device.output("Ignored pending test")
        device.output("</div>")
    }

    override fun finish() {
        //TODO fixme
        device.output("")
    }
}

