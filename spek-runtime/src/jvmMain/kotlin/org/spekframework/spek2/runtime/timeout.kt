package org.spekframework.spek2.runtime

actual fun getGlobalTimeoutSetting(default: Long): Long {
    var override = System.getProperty("SPEK_TIMEOUT")?.toLong()
    if (override == null) {
        override = System.getProperty("spek2.timeout")?.toLong()
    }
    return override ?: default
}

actual fun isEnableConcurrentDiscovery(default: Boolean): Boolean {
    return System.getProperty("spek2.discovery.concurrent") != null || default
}