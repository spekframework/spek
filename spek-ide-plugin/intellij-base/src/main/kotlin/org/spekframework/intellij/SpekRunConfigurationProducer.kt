package org.spekframework.intellij

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPackage
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.kotlin.config.KotlinFacetSettings
import org.jetbrains.kotlin.config.KotlinFacetSettingsProvider
import org.jetbrains.kotlin.config.KotlinSourceRootType
import org.jetbrains.kotlin.idea.caches.project.implementingModules
import org.jetbrains.kotlin.idea.core.getPackage
import org.jetbrains.kotlin.platform.IdePlatformKind
import org.jetbrains.kotlin.platform.impl.CommonIdePlatformKind
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.spekframework.intellij.domain.ScopeDescriptorCache
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
            val path = if (it is PsiDirectory) {
                getPackagePath(context, it)
            } else {
                val elementContext = maybeGetContext(it)
                val descriptor = when (elementContext) {
                    is KtClassOrObject -> descriptorCache.fromClassOrObject(elementContext)
                    is KtCallExpression -> descriptorCache.fromCallExpression(elementContext)
                    else -> null
                }

                if (descriptor != null && !descriptor.excluded && descriptor.runnable) {
                    descriptor.path
                } else {
                    null
                }
            }

            configuration.data.path == path
        } ?: false
    }

    override fun setupConfigurationFromContext(configuration: SpekRunConfiguration<*>,
                                               context: ConfigurationContext,
                                               sourceElement: Ref<PsiElement>): Boolean {
        val descriptorCache = checkNotNull(
            context.project.getComponent(ScopeDescriptorCache::class.java)
        )

        return sourceElement.get().let {
            val path = if (it is PsiDirectory) {
                getPackagePath(context, it)
            } else {
                val elementContext = maybeGetContext(it)
                val descriptor = when (elementContext) {
                    is KtClassOrObject -> descriptorCache.fromClassOrObject(elementContext)
                    is KtCallExpression -> descriptorCache.fromCallExpression(elementContext)
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
    }

    private fun getPackagePath(context: ConfigurationContext, dir: PsiDirectory): Path? {
        if (context.module != null) {
            val moduleRootManager = ModuleRootManager.getInstance(context.module)
            val roots = moduleRootManager.getSourceRoots(JavaSourceRootType.TEST_SOURCE)

            if (VfsUtil.isUnder(dir.virtualFile, roots.toSet())) {
                val psiPackage = dir.getPackage()

                if (psiPackage != null && psiPackage.qualifiedName.isNotEmpty()) {
                    val parts = psiPackage.qualifiedName.split(".")
                    val builder = PathBuilder()

                    parts.forEach {
                        builder.append(it)
                    }
                    return builder.build()
                } else {
                    return PathBuilder().build()
                }
            }
        }
        return null
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
