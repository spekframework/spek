package org.spekframework.intellij

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.KotlinFacetSettings
import org.jetbrains.kotlin.config.KotlinFacetSettingsProvider
import org.jetbrains.kotlin.idea.caches.project.implementingModules
import org.jetbrains.kotlin.platform.IdePlatformKind
import org.jetbrains.kotlin.platform.impl.CommonIdePlatformKind
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.spekframework.intellij.domain.ScopeDescriptorCache
import org.spekframework.intellij.util.maybeGetContext

abstract class SpekRunConfigurationProducer(val producerType: ProducerType, type: SpekConfigurationType)
    : RunConfigurationProducer<SpekRunConfiguration<*>>(type) {
    override fun isConfigurationFromContext(configuration: SpekRunConfiguration<*>,
                                            context: ConfigurationContext): Boolean {
        val descriptorCache = checkNotNull(
            context.project.getComponent(ScopeDescriptorCache::class.java)
        )
        val descriptor = context.psiLocation?.let {
            val elementContext = maybeGetContext(it)
            when (elementContext) {
                is KtClassOrObject -> descriptorCache.fromClassOrObject(elementContext)
                is KtCallExpression -> descriptorCache.fromCallExpression(elementContext)
                else -> null
            }
        }

        return configuration.data.path == descriptor?.path
    }

    override fun setupConfigurationFromContext(configuration: SpekRunConfiguration<*>,
                                               context: ConfigurationContext,
                                               sourceElement: Ref<PsiElement>): Boolean {
        val descriptorCache = checkNotNull(
            context.project.getComponent(ScopeDescriptorCache::class.java)
        )
        val path = sourceElement.get().let {
            val elementContext = maybeGetContext(it)
            when (elementContext) {
                is KtClassOrObject -> descriptorCache.fromClassOrObject(elementContext)
                is KtCallExpression -> descriptorCache.fromCallExpression(elementContext)
                else -> null
            }
        }?.path
        return if (path != null) {
            configuration.data.path = path
            val kotlinFacetSettings = KotlinFacetSettingsProvider.getInstance(context.project)
                .getInitializedSettings(context.module)


            var canRun = false
            if (isPlatformSupported(kotlinFacetSettings.platform!!.kind)) {
                configuration.configureForModule(context.module)
                canRun = true
            } else if (kotlinFacetSettings.platform!!.kind == CommonIdePlatformKind) {
                val result = findSupportedModule(context.project, context.module)
                if (result != null) {
                    val (module, moduleKotlinFacetSettings) = result
                    configuration.configureForModule(module)
                    configuration.data.producerType = moduleKotlinFacetSettings.platform!!.kind.toProducerType()
                    canRun = true
                }
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
                isPlatformSupported(it.second.platform!!.kind)
            }
    }

    private fun isPlatformSupported(kind: IdePlatformKind<*>) = kind.toProducerType() == producerType
}