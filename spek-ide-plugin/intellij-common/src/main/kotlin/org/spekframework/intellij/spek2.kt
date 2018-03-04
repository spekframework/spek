package org.spekframework.intellij

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfigurationModule
import com.intellij.openapi.diagnostic.Logger
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder

abstract class Spek2BaseRunConfiguration<T: RunConfigurationModule>(
    name: String,
    configurationModule: T,
    factory: ConfigurationFactory
): SpekBaseRunConfiguration<T>(name, configurationModule, factory)


val LOG = Logger.getInstance("org.spekframework.spek2")
