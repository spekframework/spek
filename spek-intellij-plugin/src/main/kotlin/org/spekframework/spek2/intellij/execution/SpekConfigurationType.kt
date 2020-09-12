package org.spekframework.spek2.intellij.execution

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeUtil
import org.jetbrains.kotlin.idea.KotlinIcons
import javax.swing.Icon

class SpekConfigurationType : ConfigurationType {
    private val factories: Array<ConfigurationFactory> by lazy {
        SpekConfigurationFactory.EXTENSION_POINT.extensionList.toTypedArray()
    }

    override fun getDisplayName(): String {
        return "Spek 2"
    }

    override fun getConfigurationTypeDescription(): String {
        return "Run Spek 2 tests"
    }

    override fun getIcon(): Icon {
        return KotlinIcons.SMALL_LOGO_13
    }

    override fun getId(): String {
        return "org.spekframework.spek2"
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return factories
    }

    companion object {
        fun getInstance(): SpekConfigurationType {
            return ConfigurationTypeUtil.findConfigurationType(SpekConfigurationType::class.java)
        }
    }
}