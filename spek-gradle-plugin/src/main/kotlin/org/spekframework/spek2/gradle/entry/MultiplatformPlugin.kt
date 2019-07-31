package org.spekframework.spek2.gradle.entry

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
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
        project.afterEvaluate { project.configureSpek() }
    }

    private fun Project.configureSpek() {
        val spekExtension = checkNotNull(project.extensions.findByType(MultiplatformExtension::class.java))
        val kotlinMppExtension = checkNotNull(project.extensions.findByType(KotlinMultiplatformExtension::class.java))

        if (!spekExtension.enabled) {
            return
        }

        kotlinMppExtension.targets.forEach { target ->
            target.compilations
                    .filterIsInstance(KotlinNativeCompilation::class.java)
                    .filter { it.isTestCompilation }
                    .forEach { compilation ->
                        disableKotlinTest(compilation)
                        configureSpekRunner(target)
                        addSpekRuntimeDependency(compilation)
                    }
        }
    }

    private fun disableKotlinTest(compilation: KotlinNativeCompilation) {
        compilation.isTestCompilation = false
    }

    private fun configureSpekRunner(target: KotlinTarget) {
        if (target !is KotlinNativeTarget) {
            throw UnsupportedOperationException("Expected target ${target.name} to be a KotlinNativeTarget")
        }

        target.binaries
                .filterIsInstance<Executable>()
                .forEach { binary ->
                    binary.entryPoint = "org.spekframework.spek2.launcher.spekMain"
                }
    }

    private fun addSpekRuntimeDependency(compilation: KotlinNativeCompilation) {
        compilation.defaultSourceSet.dependencies {
            implementation("$spekMavenGroup:spek-runtime:$spekVersion")
        }
    }

    companion object {
        val spekMavenGroup = "org.spekframework.spek2"
        val spekVersion = MultiplatformPlugin::class.java.`package`.implementationVersion
    }
}
