package org.spekframework.spek2.runtime

actual fun getGlobalTimeoutSetting(default: Long): Long {
    return default
}

actual fun isParallelDiscoveryEnabled(default: Boolean): Boolean {
    return default
}

actual fun isParallelExecutionEnabled(default: Boolean): Boolean {
    return default
}