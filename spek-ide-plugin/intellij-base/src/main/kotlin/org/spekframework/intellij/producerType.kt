package org.spekframework.intellij

import org.jetbrains.kotlin.platform.IdePlatformKind
import org.jetbrains.kotlin.platform.impl.CommonIdePlatformKind
import org.jetbrains.kotlin.platform.impl.JvmIdePlatformKind

enum class ProducerType {
    COMMON,
    JVM
}

fun IdePlatformKind<*>.toProducerType(): ProducerType {
    return when (this) {
        CommonIdePlatformKind -> ProducerType.COMMON
        JvmIdePlatformKind -> ProducerType.JVM
        else -> throw IllegalArgumentException("Unsupported platform kind: ${this}")
    }
}
