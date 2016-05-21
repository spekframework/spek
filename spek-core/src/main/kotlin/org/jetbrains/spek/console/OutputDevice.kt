package org.jetbrains.spek.console

import java.io.BufferedWriter
import java.io.Closeable
import java.io.File
import java.io.PrintWriter

interface OutputDevice {
    fun outputLine(message: String)
    fun output(message: String)
}

class FileOutputDevice(filename: String) : OutputDevice, Closeable {
    val writer = BufferedWriter(PrintWriter(File(filename)))

    override fun close() {
        writer.close()
    }

    override fun outputLine(message: String) {
        writer.write(message)
        writer.write("\n")
        writer.flush()
    }

    override fun output(message: String) {
        writer.write(message)
    }
}

class ConsoleOutputDevice : OutputDevice {
    override fun outputLine(message: String) {
        println(message)
    }

    override fun output(message: String) {
        print(message)
    }
}
