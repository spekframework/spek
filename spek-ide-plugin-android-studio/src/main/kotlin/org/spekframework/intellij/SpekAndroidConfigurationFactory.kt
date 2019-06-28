package org.spekframework.intellij

import com.intellij.compiler.options.CompileStepBeforeRun
import com.intellij.execution.BeforeRunTask
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.JavaRunConfigurationModule
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key

class SpekAndroidConfigurationFactory(type: ConfigurationType): ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        SpekAndroidRunConfiguration("Un-named", JavaRunConfigurationModule(project, true), this)

    override fun configureBeforeRunTaskDefaults(providerID: Key<out BeforeRunTask<BeforeRunTask<*>>>,
                                                task: BeforeRunTask<out BeforeRunTask<*>>) {
        // disable built-in compile task, project will be built by gradle.
        if (providerID == CompileStepBeforeRun.ID) {
            task.isEnabled = false
        }
    }
}
