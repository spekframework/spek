package org.spekframework.spek2.intellij.execution

import com.intellij.execution.CommonProgramRunConfigurationParameters
import com.intellij.execution.configurations.LocatableConfiguration
import com.intellij.openapi.module.Module
import org.spekframework.spek2.intellij.support.SpekCommonProgramRunConfigurationParameters
import org.spekframework.spek2.intellij.support.SpekCommonProgramRunConfigurationParameters.GeneratedNameHint
import org.spekframework.spek2.runtime.scope.isRoot

interface SpekRunConfiguration<T: SpekCommonProgramRunConfigurationParameters>: LocatableConfiguration, CommonProgramRunConfigurationParameters {
    val data: T
    fun configureForModule(module: Module)

    override fun getEnvs(): MutableMap<String, String> {
        return data.envs
    }

    override fun isPassParentEnvs(): Boolean {
        return data.isPassParentEnvs
    }

    override fun setProgramParameters(programParameters: String?) {
        data.programParameters = programParameters
    }

    override fun setWorkingDirectory(value: String?) {
        data.workingDirectory = value
    }

    override fun setEnvs(envs: MutableMap<String, String>) {
        data.envs = envs
    }

    override fun setPassParentEnvs(passParentEnvs: Boolean) {
        data.isPassParentEnvs = passParentEnvs
    }

    override fun getProgramParameters(): String? {
        return data.programParameters ?: ""
    }

    override fun suggestedName(): String {
        val path = data.path

        val nameHint = data.generatedNameHint
        if (nameHint == null) {
            return if (path.isRoot) {
                "Spek tests in <default package>"
            } else {
                "Spek test(s): $path"
            }.trim()
        }

        return when (nameHint) {
            GeneratedNameHint.PACKAGE -> {
                val pkg = if (path.isRoot) {
                    "<default package>"
                } else {
                    path.toString().replace("/", ".")
                }
                "Spek tests in $pkg"
            }
            GeneratedNameHint.CLASS -> {
                val clz = path.name
                val pkg = path.parent?.let {
                    it.toString().replace("/", ".")
                }

                if (pkg != null) {
                    "Spek test: $pkg.$clz"
                } else {
                    "Spek test: $clz"
                }
            }
            else -> "Scope: ${path.name}"
        }
    }
}