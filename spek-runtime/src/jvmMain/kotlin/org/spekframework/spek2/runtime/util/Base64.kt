package org.spekframework.spek2.runtime.util

actual object Base64 {
    actual fun encodeToString(text: String): String {
        return java.util.Base64.getEncoder().encodeToString(text.toByteArray())
    }

    actual fun decodeToString(encodedText: String): String {
        return String(
            java.util.Base64.getDecoder().decode(encodedText.toByteArray())
        )
    }
}