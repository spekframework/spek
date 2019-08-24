package org.spekframework.spek2.gradle.domain

import org.gradle.api.model.ObjectFactory
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

class SpekTest(val name: String, objects: ObjectFactory) {
    val target = objects.property(KotlinTarget::class.java)
    val compilation = objects.property(KotlinCompilation::class.java)
}