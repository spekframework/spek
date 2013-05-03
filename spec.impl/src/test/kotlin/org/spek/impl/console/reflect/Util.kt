package org.spek.impl.console.reflect

import org.spek.impl.console.output.OutputDevice

class BufferedOutputDevice(buffer: StringBuilder): OutputDevice {
    val buffer = buffer

    override fun output(message: String) {
        buffer.append(message).append("\n")
    }
}