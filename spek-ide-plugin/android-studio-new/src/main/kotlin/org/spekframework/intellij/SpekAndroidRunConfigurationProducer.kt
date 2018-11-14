package org.spekframework.intellij

import com.intellij.execution.configurations.ConfigurationTypeUtil

class SpekAndroidRunConfigurationProducer: SpekRunConfigurationProducer(
    ProducerType.JVM,
    ConfigurationTypeUtil.findConfigurationType(SpekAndroidConfigurationType::class.java)
)
