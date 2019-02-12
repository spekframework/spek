package org.spekframework.spek2.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.Executable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

class SpekPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("spek", SpekExtension::class.java)

        project.afterEvaluate { project.configureSpek() }
    }

    private fun Project.configureSpek() {
        val spekExtension = project.extensions.findByType(SpekExtension::class.java)

        if (spekExtension != null && spekExtension.enabled == false) {
            return
        }

        val kotlinExtension = project.extensions.findByType(KotlinMultiplatformExtension::class.java)

        if (kotlinExtension != null) {
            kotlinExtension.targets.forEach { target ->
                target.compilations
                        .filterIsInstance(KotlinNativeCompilation::class.java)
                        .filter { it.isTestCompilation }
                        .forEach {
                            it.disableKotlinTest()
                            it.configureSpekRunner(target)
                        }
            }
        }
    }

    private fun KotlinNativeCompilation.disableKotlinTest() {
        this.isTestCompilation = false
    }

    private fun KotlinNativeCompilation.configureSpekRunner(target: KotlinTarget) {
        if (target !is KotlinNativeTarget) {
            throw UnsupportedOperationException("Expected target ${target.name} to be a KotlinNativeTarget")
        }

        target.binaries
                .filterIsInstance<Executable>()
                .forEach { binary ->
                    binary.entryPoint = "org.spekframework.spek2.launcher.spekMain"
                }
    }

    companion object {
        val spekVersion = SpekPlugin::class.java.`package`.implementationVersion
    }
}
