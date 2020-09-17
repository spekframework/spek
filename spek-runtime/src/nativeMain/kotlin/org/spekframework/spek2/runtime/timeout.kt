package org.spekframework.spek2.runtime

import kotlin.time.ExperimentalTime
import kotlin.time.MonoClock
import kotlin.time.measureTime

actual fun getGlobalTimeoutSetting(default: Long): Long {
    return default
}

actual fun isConcurrentDiscoveryEnabled(default: Boolean): Boolean {
    return default
}

actual fun isConcurrentExecutionEnabled(default: Boolean): Boolean {
    return default
}

@UseExperimental(ExperimentalTime::class)
actual fun measureTime(block: () -> Unit): Long {
    return MonoClock.measureTime(block).inMilliseconds.toLong()
}

actual fun getExecutionParallelism(): Int {
    return 1
}