package org.spekframework.intellij

import com.android.tools.idea.testartifacts.scopes.AndroidJunitPatcher
import com.intellij.execution.configurations.JavaParameters
import com.intellij.openapi.module.Module

class SpekAndroidParameterPatcher: AndroidJunitPatcher(), SpekJvmParameterPatcher {
    override fun patch(module: Module, parameters: JavaParameters) {
        patchJavaParameters(module, parameters)
    }
}

