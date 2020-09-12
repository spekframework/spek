package org.spekframework.spek2.intellij.execution.jvm

import com.intellij.execution.configurations.JavaRunConfigurationModule
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import org.spekframework.spek2.intellij.execution.SpekConfigurationFactory
import org.spekframework.spek2.intellij.execution.SpekConfigurationType
import org.spekframework.spek2.intellij.support.ProducerType

class SpekJvmConfigurationFactory: SpekConfigurationFactory(ProducerType.JVM) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return SpekJvmRunConfiguration("Un-named", JavaRunConfigurationModule(project, true), this)
    }
}