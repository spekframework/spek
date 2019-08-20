package org.spekframework.spek2.gradle.domain

import org.gradle.api.Project

open class MultiplatformExtension(project: Project) {
    var enabled = true
    val tests = project.container(SpekTest::class.java)
}
