package org.jetbrains.spek.api.dsl

import java.io.Closeable

interface TestBody {
    fun onCleanup(block: () -> Unit)

    fun <T : Closeable?> T.autoCleanup(): T = apply { if (this != null) onCleanup { close() } }

// When JDK 7 is officially supported:
//    fun <T : AutoCloseable?> T.autoCleanup(): T = apply { if (this != null) onCleanup { close() } }
}
