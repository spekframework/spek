package org.spekframework.spek2.gradleplugin

import com.google.auto.service.AutoService
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

@AutoService(KotlinGradleSubplugin::class)
class SpekSubplugin : KotlinGradleSubplugin<AbstractCompile> {
    override fun apply(project: Project, kotlinCompile: AbstractCompile, javaCompile: AbstractCompile?, variantData: Any?, androidProjectHandler: Any?, kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?): List<SubpluginOption> {
        val extension = project.extensions.findByType(SpekExtension::class.java) ?: SpekExtension()

        return listOf(SubpluginOption("enabled", extension.enabled.toString()))
    }

    override fun getCompilerPluginId(): String {
        return "spek"
    }

    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact(
                SpekPlugin.spekMavenGroup,
                "spek-kotlin-compiler-plugin",
                SpekPlugin.spekVersion
        )
    }

    override fun getNativeCompilerPluginArtifact(): SubpluginArtifact? = getPluginArtifact()

    override fun isApplicable(project: Project, task: AbstractCompile): Boolean
            = project.plugins.hasPlugin(SpekPlugin::class.java) && (task is KotlinNativeCompile || task is KotlinNativeLink)
}
