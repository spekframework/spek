package org.spekframework.spek2.launcher

actual class ConsoleLauncher: AbstractConsoleLauncher() {
    override fun parseArgs(args: List<String>): LauncherArgs {
        return LauncherArgs(
            listOf(ConsoleReporterType(ConsoleReporterType.Format.BASIC)),
            emptyList(),
            true
        )
    }
}