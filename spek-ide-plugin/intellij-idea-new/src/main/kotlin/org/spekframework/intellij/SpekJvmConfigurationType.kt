package org.spekframework.intellij

import com.intellij.execution.configurations.ConfigurationTypeBase
import org.jetbrains.kotlin.idea.KotlinIcons

class SpekJvmConfigurationType: ConfigurationTypeBase(
    "org.spekframework.spek2-jvm",
    "Spek 2 - JVM",
    "Run Spek 2 tests",
    KotlinIcons.SMALL_LOGO_13
), SpekConfigurationType {
    init {
        addFactory(SpekJvmConfigurationFactory(this))
    }
}