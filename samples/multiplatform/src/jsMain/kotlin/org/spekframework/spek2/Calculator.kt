package org.spekframework.spek2

actual class Calculator actual constructor() {
    actual fun add(a: Int, b: Int): Int {
        return a + b
    }
}
