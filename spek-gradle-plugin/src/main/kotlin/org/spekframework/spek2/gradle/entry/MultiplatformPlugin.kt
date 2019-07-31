package org.spekframework.spek2.gradle.entry

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Executable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.spekframework.spek2.gradle.domain.MultiplatformExtension

class MultiplatformPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (project.extensions.findByType(KotlinMultiplatformExtension::class.java) == null) {
            throw GradleException("Kotlin multiplatform plugin needs to be applied first!")
        }
        project.extensions.create("spek2", MultiplatformExtension::class.java)
        project.afterEvaluate(this::doApply)
    }

    private fun doApply(project: Project) {
        val spekExtension = checkNotNull(project.extensions.findByType(MultiplatformExtension::class.java))
        val kotlinMppExtension = checkNotNull(project.extensions.findByType(KotlinMultiplatformExtension::class.java))

        if (!spekExtension.enabled) {
            return
        }

        kotlinMppExtension.targets.forEach { target ->
            when (target) {
                is KotlinNativeTarget -> configureNativeTarget(target)
            }
        }
    }

    private fun configureNativeTarget(target: KotlinNativeTarget) {
        target.compilations.filter { it.isTestCompilation }
            .forEach(this::configureNativeCompilation)
    }

    private fun configureNativeCompilation(compilation: KotlinNativeCompilation) {
        compilation.isTestCompilation = false
        compilation.target.binaries
            .filterIsInstance<Executable>()
            .forEach { binary ->
                binary.entryPoint = "org.spekframework.spek2.launcher.spekMain"
            }
        compilation.defaultSourceSet.dependencies {
            implementation("$spekMavenGroup:spek-runtime:$spekVersion")
        }
    }

    companion object {
        val spekMavenGroup = "org.spekframework.spek2"
        val spekVersion = MultiplatformPlugin::class.java.`package`.implementationVersion
    }
}
