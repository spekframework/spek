package org.spekframework.spek2.gradle.domain

import org.gradle.api.model.ObjectFactory

open class MultiplatformExtension(objects: ObjectFactory) {
    var enabled = true
    val tests = objects.domainObjectContainer(SpekTest::class.java)
}
