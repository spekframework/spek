package org.spekframework.spek2.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation

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
                            it.configureSpekRunner()
                        }
            }
        }
    }

    private fun KotlinNativeCompilation.disableKotlinTest() {
        this.isTestCompilation = false
    }

    private fun KotlinNativeCompilation.configureSpekRunner() {
        // TODO: set entrypoint
    }
}
