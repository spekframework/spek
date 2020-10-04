package org.spekframework.spek2.runtime

actual fun getGlobalTimeoutSetting(default: Long): Long {
    val override = System.getProperty("spek2.execution.test.timeout")?.toLong()
    return override ?: default
}

actual fun isParallelDiscoveryEnabled(default: Boolean): Boolean {
    return System.getProperty("spek2.discovery.parallel.enabled") != null || default
}

actual fun isParallelExecutionEnabled(default: Boolean): Boolean {
    return System.getProperty("spek2.execution.parallel.enabled") != null || default
}

actual fun getExecutionParallelism(): Int {
    return Runtime.getRuntime().availableProcessors()
}

actual fun isDebuggingEnabled(default: Boolean): Boolean {
    return System.getProperty("spek2.debug.enabled") != null || default
}