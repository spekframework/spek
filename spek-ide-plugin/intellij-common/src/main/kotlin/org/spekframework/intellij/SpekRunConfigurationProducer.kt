package org.spekframework.intellij

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

class SpekRunConfigurationProducer: RunConfigurationProducer<SpekBaseRunConfiguration<*>>(
    ConfigurationTypeUtil.findConfigurationType(SpekBaseConfigurationType::class.java)
) {
    override fun isConfigurationFromContext(configuration: SpekBaseRunConfiguration<*>,
                                            context: ConfigurationContext): Boolean {
        val path = context.psiLocation?.let { extractPath(it, true) }
        return configuration.path == path
    }

    override fun setupConfigurationFromContext(configuration: SpekBaseRunConfiguration<*>,
                                               context: ConfigurationContext,
                                               sourceElement: Ref<PsiElement>): Boolean {
        val path = sourceElement.get().let { extractPath(it, true) }
        return if (path != null) {
            configuration.path = path
            configuration.setGeneratedName()
            configuration.setModule(context.module)
            true
        } else {
            false
        }
    }

}
