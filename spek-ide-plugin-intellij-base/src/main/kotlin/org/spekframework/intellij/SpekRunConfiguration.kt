package org.spekframework.intellij

import com.intellij.execution.CommonProgramRunConfigurationParameters
import com.intellij.execution.configurations.LocatableConfiguration
import com.intellij.openapi.module.Module
import org.spekframework.intellij.support.SpekCommonProgramRunConfigurationParameters
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
        val parent = path.parent

        val prefix = data.producerType?.let {
            "($it)"
        } ?: ""

        return if (path.name.isEmpty() && parent != null && parent.isRoot) {
            "$prefix Spek tests in <default package>"
        } else if (parent != null && parent.isRoot) {
            "$prefix Spek tests in ${path.name}"
        } else {
            "$prefix ${path.name}"
        }.trim()
    }
}