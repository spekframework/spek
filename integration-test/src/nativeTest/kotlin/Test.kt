import kotlin.system.exitProcess
import org.spekframework.spek2.integration.*
import org.spekframework.spek2.launcher.ConsoleLauncher
import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import org.spekframework.spek2.runtime.discovery.addClass

fun main(args: Array<String>) {
    val discoveryContext = DiscoveryContext.builder()
            .addClass { CalculatorSpecs }
            .addClass { EmptyGroupTest }
            .addClass { CalculatorSpecs }
            .addClass { MemoizedTests }
            .addClass { NonUniquePathTest }
            .addClass { SetFeature }
            .addClass { SetSpec }
            .addClass { SkipTest }
            .build()

    val launcher = ConsoleLauncher()
    val exitCode = launcher.launch(discoveryContext, args.toList())
    exitProcess(exitCode)
}
