package org.spekframework.jvm

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.spekframework.runtime.DelegatingExecutionListener
import org.spekframework.runtime.runner.ConsolePrinter
import org.spekframework.runtime.runner.ConsoleRunner
import org.spekframework.runtime.runner.RunConfig
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

class JvmConsoleRunner: ConsoleRunner() {
    override fun createRuntime() = SpekJvmRuntime()
}

class ParsedArguments(parser: ArgParser) {
    val path by parser.positional("path").default("")
}

class JvmConsolePrinter: ConsolePrinter() {
    override fun extractStacktrace(throwable: Throwable): String {
        val outputStream = ByteArrayOutputStream()
        throwable.printStackTrace(PrintStream(outputStream))

        return outputStream.toString(StandardCharsets.UTF_8.name())
    }

}

fun main(args: Array<String>) {
    val printer = JvmConsolePrinter()
    val runner = JvmConsoleRunner()
    val recorder = DelegatingExecutionListener(listOf(printer))
    ParsedArguments(ArgParser(args)).apply {
        val config = RunConfig(JvmPath.from(path), recorder)
        runner.execute(config)
    }
}
