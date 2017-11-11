package org.spekframework.intellij

import com.intellij.execution.CommonProgramRunConfigurationParameters
import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ModuleBasedConfiguration
import com.intellij.execution.configurations.RunConfigurationModule
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element

abstract class SpekBaseRunConfiguration<T: RunConfigurationModule>(name: String,
                                                                   configurationModule: T,
                                                                   factory: ConfigurationFactory)
    : ModuleBasedConfiguration<T>(name, configurationModule, factory), CommonProgramRunConfigurationParameters {

    private var workingDirectory: String? = null
    private var envs = mutableMapOf<String, String>()
    private var passParentEnvs: Boolean = false
    private var programParameters: String? = null

    var path: String = ""

    override fun getWorkingDirectory() = workingDirectory

    override fun getEnvs() = envs

    override fun setWorkingDirectory(value: String?) {
        workingDirectory = value
    }

    override fun setEnvs(envs: MutableMap<String, String>) {
        this.envs = envs
    }

    override fun isPassParentEnvs() = passParentEnvs

    override fun setPassParentEnvs(passParentEnvs: Boolean) {
        this.passParentEnvs = passParentEnvs
    }

    override fun setProgramParameters(value: String?) {
        programParameters = value
    }

    override fun getProgramParameters() = programParameters

    override fun writeExternal(element: Element) {
        JDOMExternalizerUtil.writeField(element, PASS_PARENT_ENVS, passParentEnvs.toString())
        JDOMExternalizerUtil.writeField(element, WORKING_DIRECTORY, workingDirectory ?: "")
        JDOMExternalizerUtil.writeField(element, PROGRAM_PARAMETERS, programParameters ?: "")
        JDOMExternalizerUtil.writeField(element, PATH, path)
        EnvironmentVariablesComponent.writeExternal(element, envs)
    }

    override fun readExternal(element: Element) {
        JDOMExternalizerUtil.readField(element, PASS_PARENT_ENVS)?.let {
            passParentEnvs = it.toBoolean()
        }
        workingDirectory = JDOMExternalizerUtil.readField(element, WORKING_DIRECTORY)
        programParameters = JDOMExternalizerUtil.readField(element, PROGRAM_PARAMETERS)
        path = JDOMExternalizerUtil.readField(element, PATH, "")
        EnvironmentVariablesComponent.readExternal(element, envs)
    }

    companion object {
        const val PASS_PARENT_ENVS = "passParentEnvs"
        const val WORKING_DIRECTORY = "workingDirectory"
        const val PROGRAM_PARAMETERS = "programParameters"
        const val PATH = "path"
    }
}
