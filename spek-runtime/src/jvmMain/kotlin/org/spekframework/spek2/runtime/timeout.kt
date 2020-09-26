package org.spekframework.spek2.runtime

import kotlin.system.measureTimeMillis

actual fun getGlobalTimeoutSetting(default: Long): Long {
    var override = System.getProperty("SPEK_TIMEOUT")?.toLong()
    if (override == null) {
        override = System.getProperty("spek2.execution.test.timeout")?.toLong()
    } else {
        println("SPEK2_TIMEOUT is deprecated please use spek2.execution.test.timeout instead.")
    }
    return override ?: default
}

actual fun isConcurrentDiscoveryEnabled(default: Boolean): Boolean {
    return System.getProperty("spek2.discovery.parallel.enabled") != null || default
}

actual fun isConcurrentExecutionEnabled(default: Boolean): Boolean {
    return System.getProperty("spek2.execution.parallel.enabled") != null || default
}

actual fun measureTime(block: () -> Unit): Long {
    return measureTimeMillis(block)
}

actual fun isDebuggingEnabled(default: Boolean): Boolean {
    return System.getProperty("spek2.debug.enabled") != null || default
}

actual fun getExecutionParallelism(): Int {
    return Runtime.getRuntime().availableProcessors()
}