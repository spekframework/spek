package org.spekframework.ide

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import org.spekframework.spek2.runtime.JvmDiscoveryContextFactory
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.PathBuilder
import kotlin.system.exitProcess

class Spek2ConsoleLauncher {
    fun run(args: LauncherArgs) {
        try {
            val paths = args.paths.map {
                PathBuilder.parse(it)
                    .build()
            }
            val context = JvmDiscoveryContextFactory.create(args.sourceDirs.toList())
            val discoveryRequest = DiscoveryRequest(context, paths)

            val runtime = SpekRuntime()

            val discoveryResult = runtime.discover(discoveryRequest)
            val executionRequest = ExecutionRequest(discoveryResult.roots, ServiceMessageAdapter())
            runtime.execute(executionRequest)
            // forces the jvm to shutdown even if there are non-daemon threads running.
            // Ideally libraries should not leave non-daemon threads running after exit, unfortunately,
            // this is quite common even with newer libraries.
            exitProcess(0)
        } catch (e: Throwable) {
            println("An internal error has occurred.")
            e.printStackTrace()
        }
    }
}

class LauncherArgs(parser: ArgParser) {
    val sourceDirs by parser.adding("--sourceDirs", help="Spec source dirs")
    val paths by parser.adding("--paths", help = "Spek paths to execute")
}

fun main(args: Array<String>) = mainBody {
    val launcherArgs = ArgParser(args).parseInto(::LauncherArgs)
    Spek2ConsoleLauncher().run(launcherArgs)
}
