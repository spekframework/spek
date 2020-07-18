package org.spekframework.spek2.gradle.domain

import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

open class SpekTest(val name: String, objects: ObjectFactory) {
    val target = objects.property(KotlinTarget::class.java)
    val compilation = objects.property(KotlinCompilation::class.java)


    internal var useJUnitPlatform = false
    internal var junitPlatformConfigure: JUnitPlatformOptions.() -> Unit = {}

    fun useJUnitPlatform(configure: JUnitPlatformOptions.() -> Unit) {
        useJUnitPlatform = true
        junitPlatformConfigure = configure
    }

    fun useJUnitPlatform() {
        useJUnitPlatform = true
    }
}