package org.spekframework.intellij

import com.intellij.execution.configurations.ConfigurationTypeUtil

class SpekJvmRunConfigurationProducer: SpekRunConfigurationProducer(
    ProducerType.JVM,
    ConfigurationTypeUtil.findConfigurationType(SpekJvmConfigurationType::class.java)
)
