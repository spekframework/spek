package org.spekframework.intellij

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPackage
import org.jetbrains.kotlin.config.KotlinFacetSettings
import org.jetbrains.kotlin.config.KotlinFacetSettingsProvider
import org.jetbrains.kotlin.idea.caches.project.implementingModules
import org.jetbrains.kotlin.idea.core.getPackage
import org.jetbrains.kotlin.idea.util.module
import org.jetbrains.kotlin.platform.IdePlatformKind
import org.jetbrains.kotlin.platform.impl.CommonIdePlatformKind
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.spekframework.intellij.domain.ScopeDescriptorCache
import org.spekframework.intellij.support.SpekCommonProgramRunConfigurationParameters.GeneratedNameHint
import org.spekframework.intellij.util.maybeGetContext
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder

abstract class SpekRunConfigurationProducer(val producerType: ProducerType, type: SpekConfigurationType)
    : RunConfigurationProducer<SpekRunConfiguration<*>>(type) {
    override fun isConfigurationFromContext(configuration: SpekRunConfiguration<*>,
                                            context: ConfigurationContext): Boolean {
        val descriptorCache = checkNotNull(
            context.project.getComponent(ScopeDescriptorCache::class.java)
        )

        return context.psiLocation?.let {
            var nameHint: GeneratedNameHint? = null
            val path = if (it is PsiDirectory) {
                getPathFromDir(context, it)
                nameHint = GeneratedNameHint.PACKAGE
            } else if (it is PsiPackage) {
                getPathFromPackage(it)
                nameHint = GeneratedNameHint.PACKAGE
            } else {
                val elementContext = maybeGetContext(it)
                val descriptor = when (elementContext) {
                    is KtClassOrObject -> {
                        nameHint = GeneratedNameHint.CLASS
                        descriptorCache.fromClassOrObject(elementContext)
                    }
                    is KtCallExpression -> {
                        nameHint = GeneratedNameHint.SCOPE
                        descriptorCache.fromCallExpression(elementContext)
                    }
                    else -> null
                }

                if (descriptor != null && !descriptor.excluded && descriptor.runnable) {
                    descriptor.path
                } else {
                    null
                }
            }
            configuration.data.path == path && configuration.data.generatedNameHint == nameHint
        } ?: false
    }

    override fun setupConfigurationFromContext(configuration: SpekRunConfiguration<*>,
                                               context: ConfigurationContext,
                                               sourceElement: Ref<PsiElement>): Boolean {
        val descriptorCache = checkNotNull(
            context.project.getComponent(ScopeDescriptorCache::class.java)
        )

        return sourceElement.get().let {
            var nameHint: GeneratedNameHint? = null
            val path = if (it is PsiDirectory) {
                nameHint = GeneratedNameHint.PACKAGE
                getPathFromDir(context, it)
            } else if (it is PsiPackage) {
                nameHint = GeneratedNameHint.PACKAGE
                getPathFromPackage(it)
            } else {
                val elementContext = maybeGetContext(it)
                val descriptor = when (elementContext) {
                    is KtClassOrObject -> {
                        nameHint = GeneratedNameHint.CLASS
                        descriptorCache.fromClassOrObject(elementContext)
                    }
                    is KtCallExpression -> {
                        nameHint = GeneratedNameHint.SCOPE
                        descriptorCache.fromCallExpression(elementContext)
                    }
                    else -> null
                }

                if (descriptor != null && !descriptor.excluded && descriptor.runnable) {
                    descriptor.path
                } else {
                    null
                }
            }

            if (path != null) {
                configuration.data.path = path
                configuration.data.generatedNameHint = nameHint
                val kotlinFacetSettings = KotlinFacetSettingsProvider.getInstance(context.project)
                        .getInitializedSettings(context.module)


                var canRun = false
                if (isPlatformSupported(kotlinFacetSettings.platform!!.kind)) {
                    configuration.configureForModule(context.module)
                    configuration.data.producerType = producerType
                    canRun = true
                    configuration.data.producerType = producerType
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
    }

    private fun getPathFromDir(context: ConfigurationContext, dir: PsiDirectory): Path? {
        if (context.module != null) {
            val moduleRootManager = ModuleRootManager.getInstance(context.module)
            if (context.module == dir.module || moduleRootManager.isDependsOn(dir.module)) {
                val psiPackage = dir.getPackage()

                if (psiPackage != null) {
                    return getPathFromPackage(psiPackage)
                }
            }
        }
        return null
    }

    private fun getPathFromPackage(pkg: PsiPackage): Path {
        if (pkg.qualifiedName.isEmpty()) {
            return PathBuilder().build()
        }
        return PathBuilder()
            .appendPackage(pkg.qualifiedName)
            .build()
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
