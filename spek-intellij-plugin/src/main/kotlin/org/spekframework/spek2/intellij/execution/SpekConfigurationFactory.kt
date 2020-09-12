package org.spekframework.spek2.intellij.execution

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.extensions.ExtensionPointName
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeFirstWord
import org.spekframework.spek2.intellij.support.ProducerType

abstract class SpekConfigurationFactory(
    val producerType: ProducerType
): ConfigurationFactory(SpekConfigurationType.getInstance()) {
    override fun getName(): String {
        return producerType.name.toLowerCase().capitalize()
    }
    override fun getId(): String {
        return producerType.name
    }

    companion object {
        val EXTENSION_POINT = ExtensionPointName.create<SpekConfigurationFactory>("org.spekframework.spek2.configurationFactory")
    }
}