package org.spekframework.spek2.runtime.scope

expect object Base64 {
    fun encodeToString(text: String): String
    fun decodeToString(encodedText: String): String
}