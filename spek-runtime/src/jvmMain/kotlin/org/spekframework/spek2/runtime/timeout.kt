package org.spekframework.spek2.runtime

actual fun getGlobalTimeoutSetting(default: Long): Long {
    return System.getProperty("SPEK_TIMEOUT")?.toLong() ?: default
}