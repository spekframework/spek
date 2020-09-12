package org.spekframework.spek2.intellij.execution.jvm

import com.intellij.execution.CommonJavaRunConfigurationParameters
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.SettingsEditor
import org.jdom.Element
import org.spekframework.spek2.intellij.execution.SpekRunConfiguration
import org.spekframework.spek2.intellij.support.SpekJvmCommonRunConfigurationParameters

class SpekJvmRunConfiguration(name: String, module: JavaRunConfigurationModule, factory: ConfigurationFactory)
    : ModuleBasedConfiguration<JavaRunConfigurationModule, RunConfigurationOptions>(name, module, factory)
    , SpekRunConfiguration<SpekJvmCommonRunConfigurationParameters>, CommonJavaRunConfigurationParameters {
    override val data: SpekJvmCommonRunConfigurationParameters = SpekJvmCommonRunConfigurationParameters(project)

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return SpekJvmSettingsEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return SpekRunnerCommandLineState(environment, this)
    }

    override fun getValidModules(): MutableCollection<Module> {
        return mutableListOf(*ModuleManager.getInstance(project).modules)
    }

    val configurationModule: JavaRunConfigurationModule
        get() = super.getConfigurationModule()

    override fun configureForModule(module: Module) {
        setModule(module)
        setGeneratedName()
    }

    override fun writeExternal(element: Element) {
        super<ModuleBasedConfiguration>.writeExternal(element)
        data.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super<ModuleBasedConfiguration>.readExternal(element)
        data.readExternal(element)
    }

    override fun setAlternativeJrePath(path: String?) {
        data.alternativeJrePath = path
    }

    override fun setVMParameters(vmParameters: String?) {
        data.vmParameters = vmParameters
    }

    override fun isAlternativeJrePathEnabled(): Boolean {
        return data.isAlternativeJrePathEnabled
    }

    override fun getPackage(): String? {
        return data.`package`
    }

    override fun getRunClass(): String? {
        return data.runClass
    }

    override fun getWorkingDirectory(): String? {
        return data.workingDirectory
    }

    override fun setAlternativeJrePathEnabled(enabled: Boolean) {
        data.isAlternativeJrePathEnabled = enabled
    }

    override fun getVMParameters(): String {
        return data.vmParameters ?: ""
    }

    override fun getAlternativeJrePath(): String? {
        return data.alternativeJrePath
    }
}