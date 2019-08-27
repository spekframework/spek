package org.spekframework.spek2.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

open class ExecSpekTests: DefaultTask() {
    val target = project.objects.property(KotlinTarget::class.java)
    val compilation = project.objects.property(KotlinCompilation::class.java)

    @TaskAction
    fun run() {
        // do nothing
    }
}