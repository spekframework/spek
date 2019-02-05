import kotlin.system.exitProcess
import org.spekframework.spek2.integration.*
import org.spekframework.spek2.launcher.ConsoleLauncher
import org.spekframework.spek2.runtime.discovery.DiscoveryContext

fun main(args: Array<String>) {
    val discoveryContext = DiscoveryContext.builder()
        .addTest { CalculatorSpecs }
        .addTest { EmptyGroupTest }
        .addTest { CalculatorSpecs }
        .addTest { MemoizedTests }
        .addTest { NonUniquePathTest }
        .addTest { SetFeature }
        .addTest { SetSpec }
        .addTest { SkipTest }
        .build()

    val launcher = ConsoleLauncher()
    val exitCode = launcher.launch(discoveryContext, args.toList())
    exitProcess(exitCode)
}
