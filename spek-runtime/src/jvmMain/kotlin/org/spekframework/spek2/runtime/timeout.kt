package org.spekframework.spek2.runtime

import kotlin.system.measureTimeMillis

actual fun getGlobalTimeoutSetting(default: Long): Long {
    var override = System.getProperty("SPEK_TIMEOUT")?.toLong()
    if (override == null) {
        override = System.getProperty("spek2.timeout")?.toLong()
    }
    return override ?: default
}

actual fun isConcurrentDiscoveryEnabled(default: Boolean): Boolean {
    return System.getProperty("spek2.discovery.concurrent") != null || default
}

actual fun measureTime(block: () -> Unit): Long {
    return measureTimeMillis(block)
}