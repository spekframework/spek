@file:Suppress("NOTHING_TO_INLINE")

package org.spekframework.spek2.data_driven

actual inline fun String.format(vararg args: Any?): String = java.lang.String.format(this, *args)
