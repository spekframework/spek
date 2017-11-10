package org.spekframework.intellij

import com.intellij.execution.configurations.ConfigurationTypeBase
import org.jetbrains.kotlin.idea.KotlinIcons

abstract class SpekConfigurationType(id: String, displayName: String): ConfigurationTypeBase(
    id,
    displayName,
    "Run specifications",
    KotlinIcons.KOTLIN_LOGO_24
)
