package org.spekframework.spek2.gradle.kotlin

import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.spekframework.spek2.gradle.domain.MultiplatformExtension
import org.spekframework.spek2.gradle.entry.MultiplatformPlugin

class Subplugin : KotlinCompilerPluginSupportPlugin {
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val extension = checkNotNull(kotlinCompilation.target.project.extensions.findByType(MultiplatformExtension::class.java))

        return kotlinCompilation.target.project.provider {
            listOf(SubpluginOption("enabled", extension.enabled.toString()))
        }
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

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return kotlinCompilation.target.project.plugins.hasPlugin(MultiplatformPlugin::class.java)
//                && (task is KotlinNativeCompile || task is KotlinNativeLink)
    }
}
