package org.jetbrains.spek.console

import java.io.*

public interface OutputDevice {
    public fun output(message: String)
}

public class FileOutputDevice(filename: String) : OutputDevice, Closeable {
    val writer = BufferedWriter(PrintWriter(File(filename)))

    public override fun close() {
        writer.close()
    }

    override fun output(message: String) {
        writer.write(message)
        writer.write("\n")
        writer.flush()
    }
}

public class ConsoleOutputDevice : OutputDevice {
    override fun output(message: String) {
        println(message)
    }
}

class StringBuilderOutputDevice(buffer: StringBuilder) : OutputDevice {
    val buffer = buffer

    override fun output(message: String) {
        buffer.append(message).append("\n")
    }
}