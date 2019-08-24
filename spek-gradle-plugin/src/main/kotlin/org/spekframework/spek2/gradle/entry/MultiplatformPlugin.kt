package org.spekframework.spek2.gradle.entry

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.spekframework.spek2.gradle.domain.MultiplatformExtension
import org.spekframework.spek2.gradle.task.ExecSpekTests

class MultiplatformPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            val kotlinMppExtension = checkNotNull(project.extensions.findByType(KotlinMultiplatformExtension::class.java)) { "Kotlin multiplatform plugin not applied!" }
            val mppExtension = project.extensions.create("spek2", MultiplatformExtension::class.java, project.objects)
            configureTestsContainer(project, mppExtension)
            configureDefaults(project, mppExtension, kotlinMppExtension)
        }
    }

    private fun configureTestsContainer(project: Project, mppExtension: MultiplatformExtension) {
        mppExtension.tests.all {
            val spekTest = this
            project.tasks.create("spek${spekTest.name.capitalize()}", ExecSpekTests::class.java) {
                group = SPEK_GROUP
                description = "Run Spek tests for ${spekTest.name}"
                target.set(spekTest.target)
                compilation.set(spekTest.compilation)
                project.afterEvaluate {
                    val target = spekTest.target.get()
                    project.tasks.named("${target.name}SpekTests").configure {
                        dependsOn(this@create)
                    }
                    when (val compilation = spekTest.compilation.orElse(target.compilations.named("test")).get()) {
                        is KotlinNativeCompilation -> configureNativeCompilation(project, compilation, this@create, this@all.name)
                        is KotlinJvmCompilation -> configureJvmCompilation(project, compilation, this@create, this@all.name)
                    }
                }
            }
        }
    }

    private fun configureNativeCompilation(project: Project, compilation: KotlinNativeCompilation, spekTask: ExecSpekTests, testName: String) {
        compilation.target.binaries {
            executable("spek", listOf(DEBUG)) {
                this.compilation = compilation
                entryPoint = "org.spekframework.spek2.launcher.spekMain"
                runTask?.let { runTask ->
                    spekTask.dependsOn(runTask)
                }
            }
        }
    }

    private fun configureJvmCompilation(project: Project, compilation: KotlinJvmCompilation, spekTask: ExecSpekTests, testName: String) {
        project.tasks.create("runSpek${testName.capitalize()}", Test::class.java) {
            testClassesDirs = compilation.output.classesDirs
            classpath = compilation.compileDependencyFiles + compilation.runtimeDependencyFiles
            useJUnitPlatform {
                includeEngines("spek2")
            }
            spekTask.dependsOn(this)
        }
    }

    private fun configureDefaults(project: Project, mppExtension: MultiplatformExtension, kotlinMppExtension: KotlinMultiplatformExtension) {
        if (!mppExtension.enabled) {
            return
        }

        val allSpekTestsTask = project.tasks.register("allSpekTests") {
            group = VERIFICATION_GROUP
            description = "Run all Spek tests."
            // prevents gradle from skipping this task
            onlyIf { true }
            doLast {  }
        }

        kotlinMppExtension.targets.all {
            when (this) {
                is KotlinNativeTarget, is KotlinJvmTarget -> {
                    project.tasks.create("${this.name}SpekTests") {
                        group = VERIFICATION_GROUP
                        description = "Run Spek tests for target ${this@all.name}."
                        // prevents gradle from skipping this task
                        onlyIf { true }
                        doLast { }

                        allSpekTestsTask.get().dependsOn(this)
                    }

                    mppExtension.tests.create("${name}Test") {
                        target.set(this@all)
                    }

//                    this.compilations.named("test") {
//                        val kotlinCompilation = this
//                        mppExtension.tests.create("${this@all.name}${kotlinCompilation.name.capitalize()}") {
//                            target.set(this@all)
//                            compilation.set(kotlinCompilation)
//                        }
//                    }
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
