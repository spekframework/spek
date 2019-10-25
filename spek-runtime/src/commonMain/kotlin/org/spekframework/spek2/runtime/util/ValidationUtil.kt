package org.spekframework.spek2.runtime.util

import org.spekframework.spek2.lifecycle.ScopeId

fun ScopeId.isValid(): Boolean =
        name.isNotBlank() && !""" *\[\d*]""".toRegex().matches(name)