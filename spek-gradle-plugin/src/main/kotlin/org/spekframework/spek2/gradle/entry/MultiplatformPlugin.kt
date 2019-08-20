package org.spekframework.spek2.gradle.entry

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.spekframework.spek2.gradle.domain.MultiplatformExtension

class MultiplatformPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val kotlinMppExtension = checkNotNull(project.extensions.findByType(KotlinMultiplatformExtension::class.java)) { "Kotlin multiplatform plugin not applied!" }
        val mppExtension = project.extensions.create("spek2", MultiplatformExtension::class.java, project)
        configureTestsContainer(project, mppExtension)
        configureDefaults(project, mppExtension, kotlinMppExtension)
    }

    private fun configureTestsContainer(project: Project, mppExtension: MultiplatformExtension) {
        project.afterEvaluate {
            mppExtension.tests.all { spekTest ->
                val compilation = spekTest.compilation.get()
                val target = compilation.target
                val targetCheckTask = project.tasks.named("${target.name}SpekTest")
                when (compilation) {
                    is KotlinNativeCompilation -> configureNativeCompilation(project, compilation, targetCheckTask.get())
                    is KotlinJvmCompilation -> configureJvmCompilation(project, compilation, targetCheckTask.get())
                }
            }
        }
    }

    private fun configureNativeCompilation(project: Project, compilation: KotlinNativeCompilation, targetCheckTask: Task) {
        compilation.target.binaries {
            executable("spekTest", listOf(DEBUG)) {
                this.compilation = compilation
                entryPoint = "org.spekframework.spek2.launcher.spekMain"
                runTask?.let { runTask ->
                    runTask.group = SPEK_GROUP
                    targetCheckTask.dependsOn(runTask)
                }
            }
        }
    }

    private fun configureJvmCompilation(project: Project, compilation: KotlinJvmCompilation, targetCheckTask: Task) {
    }

    private fun configureDefaults(project: Project, mppExtension: MultiplatformExtension, kotlinMppExtension: KotlinMultiplatformExtension) {
        if (!mppExtension.enabled) {
            return
        }

        val allSpekTestsTask = project.tasks.register("allSpekTests") { task ->
            task.group = VERIFICATION_GROUP
            task.description = "Run all Spek tests."
            // prevents gradle from skipping this task
            task.onlyIf { true }
            task.doLast {  }
        }

        kotlinMppExtension.targets.all { target ->
            val task = project.tasks.register("${target.name}SpekTest") { task ->
                task.group = VERIFICATION_GROUP
                task.description = "Run Spek tests for target ${target.name}."
                // prevents gradle from skipping this task
                task.onlyIf { true }
                task.doLast {  }

                allSpekTestsTask.get().dependsOn(task)
            }

            when (target) {
                is KotlinNativeTarget, is KotlinJvmTarget -> {
                    target.compilations.named("test") { compilation ->
                        mppExtension.tests.create("${target.name}${compilation.name.capitalize()}") { spekTest ->
                            spekTest.compilation.set(compilation)
                        }
                    }
                }
            }
        }
    }


    companion object {
        val spekMavenGroup = "org.spekframework.spek2"
        val spekVersion = MultiplatformPlugin::class.java.`package`.implementationVersion
        private const val SPEK_GROUP = "Spek"
        private const val VERIFICATION_GROUP = "Verification"
    }
}
