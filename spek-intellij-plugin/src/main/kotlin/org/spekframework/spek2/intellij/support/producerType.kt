package org.spekframework.spek2.intellij.support

import org.jetbrains.kotlin.platform.IdePlatformKind
import org.jetbrains.kotlin.platform.impl.CommonIdePlatformKind
import org.jetbrains.kotlin.platform.impl.JsIdePlatformKind
import org.jetbrains.kotlin.platform.impl.JvmIdePlatformKind
import org.jetbrains.kotlin.platform.impl.NativeIdePlatformKind

enum class ProducerType {
    COMMON,
    JVM,
    NATIVE,
    JS
}

fun IdePlatformKind<*>.toProducerType(): ProducerType {
    return when (this) {
        CommonIdePlatformKind -> ProducerType.COMMON
        JvmIdePlatformKind -> ProducerType.JVM
        NativeIdePlatformKind -> ProducerType.NATIVE
        JsIdePlatformKind -> ProducerType.JS
        else -> throw IllegalArgumentException("Unsupported platform kind: $this")
    }
}
