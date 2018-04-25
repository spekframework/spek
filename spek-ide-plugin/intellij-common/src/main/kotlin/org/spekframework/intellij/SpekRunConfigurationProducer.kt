package org.spekframework.intellij

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.KotlinFacetSettingsProvider
import org.jetbrains.kotlin.config.TargetPlatformKind

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
            val kotlinFacetSettings = checkNotNull(
                KotlinFacetSettingsProvider.getInstance(context.project)
                    .getSettings(context.module)
            )

            if (kotlinFacetSettings.targetPlatformKind == TargetPlatformKind.Common) {
                val module = ModuleManager.getInstance(context.project)
                    .findModuleByName(checkNotNull(kotlinFacetSettings.implementedModuleName))
                configuration.setModule(module)
            } else {
                configuration.setModule(context.module)
            }
            true
        } else {
            false
        }
    }

}
