package org.spekframework.spek2.runtime

actual fun getGlobalTimeoutSetting(default: Long): Long {
    return default
}

actual fun isEnableConcurrentDiscovery(default: Boolean): Boolean {
    return default
}