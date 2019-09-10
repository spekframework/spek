package org.spekframework.spek2.runtime

actual fun getGlobalTimeoutSetting(default: Long): Long {
    val override = System.getProperty("SPEK_TIMEOUT")?.toLong()
    return override ?: default
}