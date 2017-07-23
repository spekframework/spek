package org.spekframework.gradle.jvm

import org.gradle.api.Plugin
import org.gradle.api.Project

class SpekPlugin implements Plugin<Project> {
    static final def SPEK_EXTENSION = "spek"
    static final def SPEK_CONFIGURATION = "spek"
    static final def DEFAULT_SPEK_SOURCES = "test"

    @Override
    void apply(Project project) {
        def speks = project.container(SpecSources)
        SpekPluginExtension extension = project.extensions.create(SPEK_EXTENSION, SpekPluginExtension, speks)

        setupConfiguration(project, extension)
        setupTasks(project, extension)
        setupDefaultTask(project, extension)
    }

    private def setupTasks(Project project, SpekPluginExtension extension) {
        project.afterEvaluate {
            extension.all {
                if (it.enabled) {
                    project.tasks.create("${it.name}Spek", SpekExecTask, { SpekExecTask task ->
                        def runtime = project.configurations.getByName("${it.name}Runtime")
                        def sourceSet = project.sourceSets."${it.name}"
                        task.classpath = runtime + project.configurations.getByName(SPEK_CONFIGURATION) +
                            sourceSet.output + sourceSet.compileClasspath
                    })
                }
            }
        }
    }

    private def setupDefaultTask(Project project, SpekPluginExtension extension) {
        project.afterEvaluate {
            extension.maybeCreate(DEFAULT_SPEK_SOURCES)
        }
    }

    private def setupConfiguration(Project project, SpekPluginExtension extension) {
        project.afterEvaluate {
            project.configurations.create(SPEK_CONFIGURATION)
            project.dependencies.add(SPEK_CONFIGURATION, "org.jetbrains.spek:spek-jvm-runtime:${extension.apiVersion}")
        }
    }
}
