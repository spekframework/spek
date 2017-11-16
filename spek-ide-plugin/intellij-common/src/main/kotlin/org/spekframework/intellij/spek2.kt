package org.spekframework.intellij

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfigurationModule
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder

abstract class Spek2BaseRunConfiguration<T: RunConfigurationModule>(
    name: String,
    configurationModule: T,
    factory: ConfigurationFactory
): SpekBaseRunConfiguration<T>(name, configurationModule, factory) {
    var path: Path = PathBuilder.ROOT

    override fun suggestedName() = path.toString()
}
