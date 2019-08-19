package org.spekframework.spek2.gradle.entry

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Executable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable
import org.jetbrains.kotlin.konan.util.visibleName
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
                is KotlinNativeTarget -> configureNativeTarget(project, target)
            }
        }
    }

    private fun configureNativeTarget(project: Project, target: KotlinNativeTarget) {
        val targetCheckTask = project.tasks.create("${target.name}SpekTest") { task ->
            task.group = VERIFICATION_GROUP
            task.description = "Run Spek tests for target ${target.name}"
            // prevents gradle from skipping this task
            task.onlyIf { true }
            task.doLast {  }
        }

        project.tasks.named("allTests") { allTestTask ->
            allTestTask.dependsOn(targetCheckTask)
        }

        target.binaries {
            executable("spek", listOf(DEBUG)) {
                compilation = target.compilations.getByName("test")
                entryPoint = "org.spekframework.spek2.launcher.spekMain"
                runTask?.let { runTask ->
                    runTask.group = SPEK_GROUP
                    project.tasks.create("testSpekDebug${target.name.capitalize()}") { task ->
                        task.group = SPEK_GROUP
                        task.dependsOn(runTask)
                        // prevents gradle from skipping this task
                        task.onlyIf { true }
                        task.doLast {  }
                        targetCheckTask.dependsOn(task)
                    }
                }
            }
        }

        target.compilations.forEach { compilation ->
            compilation.defaultSourceSet.dependencies {
                implementation("$spekMavenGroup:spek-runtime:$spekVersion")
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
