package org.spekframework.intellij

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.KotlinFacetSettings
import org.jetbrains.kotlin.config.KotlinFacetSettingsProvider
import org.jetbrains.kotlin.config.TargetPlatformKind
import org.jetbrains.kotlin.idea.facet.implementingModules
import org.jetbrains.kotlin.idea.run.findJvmImplementationModule

enum class ProducerType {
    COMMON,
    JVM
}

fun TargetPlatformKind<*>.toProducerType(): ProducerType {
    return when (this) {
        is TargetPlatformKind.Common -> ProducerType.COMMON
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
            if (isPlatformSupported(kotlinFacetSettings.targetPlatformKind!!)) {
                configuration.setModule(context.module)
                canRun = true
            } else  if (kotlinFacetSettings.targetPlatformKind == TargetPlatformKind.Common) {
                val result = findSupportedModule(context.project, context.module)
                if (result != null) {
                    val (module, moduleKotlinFacetSettings) = result
                    configuration.setModule(module)
                    configuration.producerType = moduleKotlinFacetSettings.targetPlatformKind!!.toProducerType()
                    canRun = true
                }
            }

            if (canRun) {
                configuration.setGeneratedName()
            }

            canRun
        } else {
            false
        }
    }

    private fun findSupportedModule(project: Project, commonModule: Module): Pair<Module, KotlinFacetSettings>? {
        val kotlinFacetSettingsProvider = KotlinFacetSettingsProvider.getInstance(project)
        return commonModule.implementingModules
            .map { it to kotlinFacetSettingsProvider.getInitializedSettings(it) }
            .firstOrNull {
                isPlatformSupported(it.second.targetPlatformKind!!)
            }
    }

    private fun isPlatformSupported(targetPlatformKind: TargetPlatformKind<*>) = targetPlatformKind.toProducerType() == producerType
}
