package org.spekframework.intellij

import com.intellij.execution.configurations.ConfigurationTypeBase
import org.jetbrains.kotlin.idea.KotlinIcons

class SpekAndroidConfigurationType: ConfigurationTypeBase(
    "Specs2RunConfiguration", // "org.spekframework.spek2-android",
    "Spek 2 - JVM",
    "Run Spek 2 tests",
    KotlinIcons.SMALL_LOGO_13
), SpekConfigurationType {
    init {
        addFactory(SpekAndroidConfigurationFactory(this))
    }
}