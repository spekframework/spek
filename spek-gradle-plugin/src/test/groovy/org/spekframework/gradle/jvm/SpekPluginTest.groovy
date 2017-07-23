package org.spekframework.gradle.jvm

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class SpekPluginTest {
    @Test
    def "should apply SpekPluginExtension"() {
        def project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.spekframework.spek-jvm'


        def extension = project.extensions.findByName("spek")
        assertTrue(extension instanceof SpekPluginExtension)
    }

    @Test
    def "should create test spek source set"() {
        def project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.spekframework.spek-jvm'

        project.afterEvaluate {
            def task = project.getTasks().findByName("testSpek")
            assertTrue(task != null)
        }
    }
}
