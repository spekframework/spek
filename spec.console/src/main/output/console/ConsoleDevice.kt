package org.spek.console.output.console

public class ConsoleDevice: OutputDevice {
    override fun output(message: String) {
        println(message)
    }
}
