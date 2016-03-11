package org.jetbrains.spek.console

import java.io.BufferedWriter
import java.io.Closeable
import java.io.File
import java.io.PrintWriter

interface OutputDevice {
    fun output(message: String)
}

class FileOutputDevice(filename: String) : OutputDevice, Closeable {
    val writer = BufferedWriter(PrintWriter(File(filename)))

    override fun close() {
        writer.close()
    }

    override fun output(message: String) {
        writer.write(message)
        writer.write("\n")
        writer.flush()
    }
}

class ConsoleOutputDevice : OutputDevice {
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