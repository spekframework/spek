package org.spekframework.intellij

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.JavaRunConfigurationModule
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

class Spek2JvmConfigurationFactory(type: ConfigurationType): ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        Spek2JvmRunConfiguration("Un-named", JavaRunConfigurationModule(project, true), this)
}

class Spek2JvmConfigurationType: SpekBaseConfigurationType(
    "org.spekframework.spek2-jvm",
    "Spek 2 - JVM"
) {
    init {
        addFactory(Spek2JvmConfigurationFactory(this))
    }
}
