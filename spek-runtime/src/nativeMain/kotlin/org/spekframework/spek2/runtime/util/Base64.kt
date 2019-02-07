package org.spekframework.spek2.runtime.util

private val BASE64_ALPHABET: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
private val BASE64_MASK: Byte = 0x3f
private val BASE64_INVERSE_MASK: Int = 0b1111_1111
private val BASE64_PAD: Char = '='
private val BASE64_PAD_BYTE: Byte = BASE64_PAD.toByte()
private val BASE64_INVERSE_ALPHABET = IntArray(256) {
    BASE64_ALPHABET.indexOf(it.toChar())
}

private fun Int.toBase64(): Char = BASE64_ALPHABET[this]


actual object Base64 {
    actual fun encodeToString(text: String): String {
        val encoded = encode(text.toByteArray())

        return buildString(encoded.size) {
            encoded.forEach { append(it.toChar()) }
        }
    }

    actual fun decodeToString(encodedText: String): String {
        val decoded = decode(encodedText.toByteArray())

        return buildString(decoded.size) {
            decoded.forEach { append(it.toChar()) }
        }
    }

    private fun encode(src: ByteArray): ByteArray {
        fun ByteArray.getOrZero(index: Int): Int = if (index >= size) 0 else get(index).toInt()
        // 4n / 3 is expected Base64 payload
        val result = ArrayList<Byte>(4 * src.size / 3)
        var index = 0
        while (index < src.size) {
            val symbolsLeft = src.size - index
            val padSize = if (symbolsLeft >= 3) 0 else (3 - symbolsLeft) * 8 / 6
            val chunk = (src.getOrZero(index) shl 16) or (src.getOrZero(index + 1) shl 8) or src.getOrZero(index + 2)
            index += 3

            for (i in 3 downTo padSize) {
                val char = (chunk shr (6 * i)) and BASE64_MASK.toInt()
                result.add(char.toBase64().toByte())
            }
            // Fill the pad with '='
            repeat(padSize) { result.add(BASE64_PAD_BYTE) }
        }

        return result.toByteArray()
    }

    fun decode(src: ByteArray): ByteArray {
        if (src.size % 4 != 0) {
            throw IllegalArgumentException("Invalid Base64 encoded data.")
        }

        if (src.size == 0) {
            return ByteArray(0)
        }

        val last = src.last()
        val secondLast = src.dropLast(1).last()

        val paddingSize = if (secondLast == BASE64_PAD_BYTE && last == BASE64_PAD_BYTE) {
            2
        } else if (last == BASE64_PAD_BYTE) {
            1
        } else {
            0
        }

        val size = (3 * src.size / 4) - paddingSize
        val result = ArrayList<Byte>(size)

        var index = 0

        while (index < src.size) {
            val isLastChunk = index == src.size - 4

            val first = decodeByte(src.get(index))
            val second = decodeByte(src.get(index + 1))
            val third = if (isLastChunk && paddingSize == 2) 0 else decodeByte(src.get(index + 2))
            val fourth = if (isLastChunk && paddingSize >= 1) 0 else decodeByte(src.get(index + 3))

            val chunk = (first shl 18) or (second shl 12) or (third shl 6) or fourth
            val paddingChars = if (isLastChunk) paddingSize else 0

            for (i in 2 downTo paddingChars) {
                val char = (chunk shr (8 * i)) and BASE64_INVERSE_MASK
                result.add(char.toByte())
            }

            index += 4
        }

        return result.toByteArray()
    }

    private fun decodeByte(byte: Byte): Int {
        val inverse = BASE64_INVERSE_ALPHABET[byte.toInt()]

        if (inverse == -1) {
            throw IllegalArgumentException("Invalid Base64 encoded data.")
        }

        return inverse
    }
}

private fun String.toByteArray() = ByteArray(length) {
    get(it).toByte()
}