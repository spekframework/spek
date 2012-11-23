import java.io.File
import java.io.PrintWriter
import java.io.FileWriter
import java.io.Closeable

public trait Device {
    fun output(message: String)
}

public class ConsoleDevice: Device {
    override fun output(message: String) {
        println(message)
    }
}

public class FileDevice(filename: String): Device, Closeable {
    val file = File(filename)

    val writer = PrintWriter(file)

    public override fun close() {
        writer.close()
    }

    override fun output(message: String) {
        writer.write(message)
        writer.flush()
    }

}