import java.io.File
import java.io.PrintWriter
import java.io.FileWriter
import java.io.Closeable

public trait OutputDevice {
    fun output(message: String)
}



