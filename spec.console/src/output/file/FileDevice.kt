package org.spek.console.output.file

import java.io.Closeable
import java.io.File
import java.io.PrintWriter
import java.io.BufferedWriter

public class FileDevice(filename: String): OutputDevice, Closeable {
    val file = File(filename)
    val writer = BufferedWriter(PrintWriter(file))

    public override fun close() {
        writer.close()
    }

    override fun output(message: String) {
        writer.write(message)
        writer.write("\n")
        writer.flush()
    }
}
