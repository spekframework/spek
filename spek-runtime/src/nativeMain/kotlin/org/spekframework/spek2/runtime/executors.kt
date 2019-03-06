package org.spekframework.spek2.runtime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun doRunBlocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking {
        block()
    }
}
