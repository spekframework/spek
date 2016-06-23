package org.jetbrains.spek.api

import java.io.Closeable

interface AssertionBody {

    fun onCleanup(block: () -> Unit)

    fun <T : Closeable?> T.autoCleanup(): T = apply { if (this != null) onCleanup { close() } }

// When JDK 7 is officially supported:
//    fun <T : AutoCloseable?> T.autoCleanup(): T = apply { if (this != null) onCleanup { close() } }

}
