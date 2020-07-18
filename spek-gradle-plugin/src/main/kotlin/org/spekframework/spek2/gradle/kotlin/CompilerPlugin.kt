package org.spekframework.spek2.gradle.kotlin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.spekframework.spek2.gradle.domain.MultiplatformExtension
import org.spekframework.spek2.gradle.entry.MultiplatformPlugin

class CompilerPlugin : KotlinCompilerPluginSupportPlugin {
    private lateinit var project: Project

    override fun apply(target: Project) {
        project = target
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        return project.provider {
            val extension = checkNotNull(project.extensions.findByType(MultiplatformExtension::class.java))
            listOf(SubpluginOption("enabled", extension.enabled.toString()))
        }
    }

    override fun getCompilerPluginId() = "spek2"

    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact(
            MultiplatformPlugin.spekMavenGroup,
            "spek-kotlin-compiler-plugin-jvm",
            MultiplatformPlugin.spekVersion
        )
    }

    override fun getPluginArtifactForNative(): SubpluginArtifact? {
        return SubpluginArtifact(
            MultiplatformPlugin.spekMavenGroup,
            "spek-kotlin-compiler-plugin-native",
            MultiplatformPlugin.spekVersion
        )
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return true
    }
}