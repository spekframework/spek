package org.spekframework.spek2.gradle.domain

import org.gradle.api.model.ObjectFactory
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import javax.inject.Inject

class SpekTest @Inject constructor(val name: String, objects: ObjectFactory) {
    val compilation = objects.property(KotlinCompilation::class.java)
}