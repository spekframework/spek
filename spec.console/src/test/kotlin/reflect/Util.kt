package org.spek.console.reflect;

import org.spek.console.output.OutputDevice

class BufferedOutputDevice(buffer: StringBuilder): OutputDevice {
    val buffer = buffer

    override fun output(message: String) {
        buffer.append(message.trim())
    }
}