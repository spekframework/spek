import java.io.Closeable
import java.io.File
import java.io.PrintWriter

public class FileDevice(filename: String): OutputDevice, Closeable {
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