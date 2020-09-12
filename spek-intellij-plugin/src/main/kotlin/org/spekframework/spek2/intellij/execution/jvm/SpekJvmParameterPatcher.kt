package org.spekframework.spek2.intellij.execution.jvm

import com.intellij.execution.configurations.JavaParameters
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.module.Module

interface SpekJvmParameterPatcher {
    fun patch(module: Module, parameters: JavaParameters)

    companion object {
        val EXTENSION_POINT = ExtensionPointName.create<SpekJvmParameterPatcher>("org.spekframework.spek2.jvm.parameterPatcher")
    }
}
