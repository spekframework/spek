package org.spekframework.spek2.launcher

import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import kotlin.reflect.KClass
import kotlin.system.exitProcess

private data class TestInfo(val cls: KClass<out Spek>, val factory: () -> Spek)
private val speks = mutableListOf<TestInfo>()

fun registerSpek(cls: KClass<out Spek>, factory: () -> Spek) {
    speks.add(TestInfo(cls, factory))
}

fun spekMain(args: Array<String>) {
    val discoveryContextBuilder = DiscoveryContext.builder()

    speks.forEach { discoveryContextBuilder.addTest(it.cls, it.factory) }

    val launcher = ConsoleLauncher()
    val exitCode = launcher.launch(discoveryContextBuilder.build(), args.toList())
    exitProcess(exitCode)
}
