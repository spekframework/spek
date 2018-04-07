package org.spekframework.ide

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.PathBuilder

class Spek2ConsoleLauncher {
    fun run(args: LauncherArgs) {
        val paths = args.paths.map {
            PathBuilder.parse(it)
                .build()
        }

        println(args.sourceDirs)
        println(paths)

        val discoveryRequest = DiscoveryRequest(args.sourceDirs.toList(), paths)

        val runtime =  SpekRuntime()

        val discoveryResult = runtime.discover(discoveryRequest)
        val executionRequest = ExecutionRequest(discoveryResult.roots, ServiceMessageAdapter())
        runtime.execute(executionRequest)
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
