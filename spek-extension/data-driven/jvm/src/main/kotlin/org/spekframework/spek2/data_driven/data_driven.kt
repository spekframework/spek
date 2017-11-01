package org.spekframework.spek2.data_driven

actual fun String.format(vararg args: Any?) = java.lang.String.format(this, args)
