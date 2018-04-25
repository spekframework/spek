package org.spekframework.intellij

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.KotlinFacetSettingsProvider
import org.jetbrains.kotlin.config.TargetPlatformKind

enum class ProducerType {
    JVM
}

fun TargetPlatformKind<*>.toProducerType(): ProducerType {
    return when (this) {
        is TargetPlatformKind.Jvm -> ProducerType.JVM
        else -> throw IllegalArgumentException("Unsupported platform kind: ${this}")
    }
}

abstract class SpekRunConfigurationProducer(val producerType: ProducerType, configurationType: SpekBaseConfigurationType): RunConfigurationProducer<SpekBaseRunConfiguration<*>>(
    configurationType
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
            val kotlinFacetSettings = KotlinFacetSettingsProvider.getInstance(context.project)
                .getInitializedSettings(context.module)

            var canRun = false
            if (kotlinFacetSettings.targetPlatformKind == TargetPlatformKind.Common) {
                val module = ModuleManager.getInstance(context.project)
                    .findModuleByName(checkNotNull(kotlinFacetSettings.implementedModuleNames.first()))
                configuration.setModule(module)
                canRun = true
            } else if (isPlatformSupported(kotlinFacetSettings.targetPlatformKind!!)) {
                configuration.setModule(context.module)
                canRun = true
            }

            canRun
        } else {
            false
        }
    }

    private fun isPlatformSupported(targetPlatformKind: TargetPlatformKind<*>) = targetPlatformKind.toProducerType() == producerType
}
