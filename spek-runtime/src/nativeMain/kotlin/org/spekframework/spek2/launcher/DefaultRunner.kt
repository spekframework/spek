package org.spekframework.spek2.launcher

import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import kotlin.native.concurrent.ThreadLocal
import kotlin.reflect.KClass
import kotlin.system.exitProcess

class TestInfo(val cls: KClass<out Spek>, val factory: () -> Spek) {
  init {
    RegisteredSpeks.add(this)
  }
}

@ThreadLocal
private object RegisteredSpeks {
  val speks = mutableListOf<TestInfo>()

  fun add(spek: TestInfo) {
    speks.add(spek)
  }
}

fun spekMain(args: Array<String>): Int {
  val discoveryContextBuilder = DiscoveryContext.builder()

   RegisteredSpeks.speks.forEach { discoveryContextBuilder.addTest(it.cls, it.factory) }

  val launcher = ConsoleLauncher()
  return launcher.launch(discoveryContextBuilder.build(), args.toList())
}

fun main(args: Array<String>) {
  exitProcess(spekMain(args))
}