package org.spek.impl.console.output.console

import org.spek.impl.console.output.OutputDevice

public class ConsoleDevice: OutputDevice {
    override fun output(message: String) {
        println(message)
    }
}
