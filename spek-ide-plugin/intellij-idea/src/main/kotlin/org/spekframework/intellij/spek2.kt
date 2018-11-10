package org.spekframework.intellij

import com.intellij.execution.configurations.*
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.idea.KotlinIcons

class Spek2JvmConfigurationFactory(type: ConfigurationType): ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        Spek2JvmRunConfiguration("Un-named", JavaRunConfigurationModule(project, true), this)
}

class Spek2JvmConfigurationType: SpekBaseConfigurationType(
    "org.spekframework.spek2-jvm",
    "Spek 2 - JVM",
    KotlinIcons.MPP
) {
    init {
        addFactory(Spek2JvmConfigurationFactory(this))
    }
}

class Spek2JvmRunConfigurationProducer: SpekRunConfigurationProducer(
    ProducerType.JVM,
    ConfigurationTypeUtil.findConfigurationType(Spek2JvmConfigurationType::class.java)
)
