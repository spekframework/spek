package org.spekframework.intellij

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.SettingsEditor
import org.jdom.Element
import org.spekframework.intellij.support.SpekJvmCommonRunConfigurationParameters
import java.util.*

class SpekJvmRunConfiguration(name: String, module: JavaRunConfigurationModule, factory: ConfigurationFactory)
    : ModuleBasedConfiguration<JavaRunConfigurationModule, RunConfigurationOptions>(name, module, factory)
    , SpekBaseJvmRunConfiguration {
    override val data: SpekJvmCommonRunConfigurationParameters = SpekJvmCommonRunConfigurationParameters(project)

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return SpekJvmSettingsEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return SpekRunnnerCommandLineState(environment, this)
    }

    override fun getValidModules(): MutableCollection<Module> {
        return mutableListOf(*ModuleManager.getInstance(project).modules)
    }

    override val configurationModule: JavaRunConfigurationModule
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
}