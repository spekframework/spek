package org.spekframework.spek2.gradle.kotlin

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
import org.spekframework.spek2.gradle.entry.MultiplatformPlugin
import org.spekframework.spek2.gradle.domain.MultiplatformExtension

@AutoService(KotlinGradleSubplugin::class)
class Subplugin : KotlinGradleSubplugin<AbstractCompile> {
    override fun apply(project: Project, kotlinCompile: AbstractCompile, javaCompile: AbstractCompile?, variantData: Any?, androidProjectHandler: Any?, kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?): List<SubpluginOption> {
        val extension = checkNotNull(project.extensions.findByType(MultiplatformExtension::class.java))

        return listOf(SubpluginOption("enabled", extension.enabled.toString()))
    }

    override fun getCompilerPluginId(): String {
        return "spek2"
    }

    override fun getPluginArtifact(): SubpluginArtifact {
        // HACK: This is a temporary workaround for https://youtrack.jetbrains.com/issue/KT-29901.
        // This plugin does nothing.

        return SubpluginArtifact(
            MultiplatformPlugin.spekMavenGroup,
            "spek-kotlin-compiler-plugin-jvm",
            MultiplatformPlugin.spekVersion
        )
    }

    override fun getNativeCompilerPluginArtifact(): SubpluginArtifact? {
        return SubpluginArtifact(
            MultiplatformPlugin.spekMavenGroup,
            "spek-kotlin-compiler-plugin-native",
            MultiplatformPlugin.spekVersion
        )
    }

    override fun isApplicable(project: Project, task: AbstractCompile): Boolean
            = project.plugins.hasPlugin(MultiplatformPlugin::class.java) && (task is KotlinNativeCompile || task is KotlinNativeLink)
}
