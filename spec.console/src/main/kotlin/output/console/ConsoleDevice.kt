package org.spek.console.output.console

import org.spek.console.output.OutputDevice

public class ConsoleDevice: OutputDevice {
    override fun output(message: String) {
        println(message)
    }
}
