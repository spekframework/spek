package org.jetbrains.spek.engine

import com.natpryce.hamkrest.anything
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.jetbrains.spek.extension.GroupExtensionContext
import org.jetbrains.spek.extension.SpekExtension
import org.jetbrains.spek.extension.TestExtensionContext
import org.jetbrains.spek.extension.execution.*
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * @author Ranie Jade Ramiso
 */
@RunWith(JUnitPlatform::class)
class ExtensionTest: AbstractSpekTestEngineTest() {
    class SpekSimpleExtension
        : BeforeExecuteTest, AfterExecuteTest,
          BeforeExecuteGroup, AfterExecuteGroup, BeforeExecuteSpec, AfterExecuteSpec {
        override fun beforeExecuteSpec(spec: GroupExtensionContext) {
            builder.appendln("${"    ".repeat(indent)}beforeExecuteSpec")
            indent++
        }

        override fun afterExecuteSpec(spec: GroupExtensionContext) {
            indent--
            builder.appendln("${"    ".repeat(indent)}afterExecuteSpec")
        }

        var indent = 0

        override fun beforeExecuteTest(test: TestExtensionContext) {
            builder.appendln("${"    ".repeat(indent)}beforeExecuteTest")
            indent++
        }

        override fun afterExecuteTest(test: TestExtensionContext) {
            indent--
            builder.appendln("${"    ".repeat(indent)}afterExecuteTest")
        }

        override fun beforeExecuteGroup(group: GroupExtensionContext) {
            builder.appendln("${"    ".repeat(indent)}beforeExecuteGroup")
            indent++
        }

        override fun afterExecuteGroup(group: GroupExtensionContext) {
            indent--
            builder.appendln("${"    ".repeat(indent)}afterExecuteGroup")
        }

        companion object {
            var builder = StringBuilder()
        }
    }

    @SpekExtension(SpekSimpleExtension::class)
    annotation class SimpleExtension

    @Test
    fun testDiscoveryCustomAnnotation() {
        @SimpleExtension
        class SomeSpek: Spek({
            test("SimpleExtension should be present") {
                assertThat(
                    (this as SpekTestEngine.Collector)
                        .registry.getExtension(SpekSimpleExtension::class), present(anything)
                )
            }
        })

        val recorder = executeTestsForClass(SomeSpek::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun testDiscovery() {
        @SpekExtension(SpekSimpleExtension::class)
        class SomeSpek: Spek({
            test("SimpleExtension should be present") {
                assertThat(
                    (this as SpekTestEngine.Collector)
                        .registry.getExtension(SpekSimpleExtension::class), present(anything)
                )
            }
        })

        val recorder = executeTestsForClass(SomeSpek::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun testLifeCycleExtensionPoints() {
        @SimpleExtension
        class SomeSpek: Spek({
            group("some group") {
                group("another group") {
                    test("test") { }
                    test("another test") { }
                }

                test("yet another test") { }
            }
        })

        SpekSimpleExtension.builder = StringBuilder()

        executeTestsForClass(SomeSpek::class)

        assertThat(SpekSimpleExtension.builder.trim().toString(), equalTo("""
        beforeExecuteSpec
            beforeExecuteGroup
                beforeExecuteGroup
                    beforeExecuteTest
                    afterExecuteTest
                    beforeExecuteTest
                    afterExecuteTest
                afterExecuteGroup
                beforeExecuteTest
                afterExecuteTest
            afterExecuteGroup
        afterExecuteSpec
        """.trimIndent()))
    }

    @Test
    fun testSubjectSpekLifeCycleExtensionPoints() {
        @SimpleExtension
        class SomeSpek: SubjectSpek<List<String>>({
            subject { emptyList() }
            group("some group") {
                group("another group") {
                    test("test") { }
                    test("another test") { }
                }

                test("yet another test") { }
            }
        })

        SpekSimpleExtension.builder = StringBuilder()

        executeTestsForClass(SomeSpek::class)

        assertThat(SpekSimpleExtension.builder.trim().toString(), equalTo("""
        beforeExecuteSpec
            beforeExecuteGroup
                beforeExecuteGroup
                    beforeExecuteTest
                    afterExecuteTest
                    beforeExecuteTest
                    afterExecuteTest
                afterExecuteGroup
                beforeExecuteTest
                afterExecuteTest
            afterExecuteGroup
        afterExecuteSpec
        """.trimIndent()))
    }

    @Test
    fun testSubjectSpekLifeCycleExtensionPointsInherited() {
        class AnotherSpec: SubjectSpek<List<String>>({
            group("another nested group") {
                test("another nested test") { }
            }
        })

        @SimpleExtension
        class SomeSpek: SubjectSpek<List<String>>({
            subject { emptyList() }
            includeSubjectSpec(AnotherSpec::class)
            group("some group") {
                group("another group") {
                    test("test") { }
                    test("another test") { }
                }

                test("yet another test") { }
            }
        })

        SpekSimpleExtension.builder = StringBuilder()

        executeTestsForClass(SomeSpek::class)

        assertThat(SpekSimpleExtension.builder.trim().toString(), equalTo("""
        beforeExecuteSpec
            beforeExecuteGroup
                beforeExecuteGroup
                    beforeExecuteTest
                    afterExecuteTest
                afterExecuteGroup
            afterExecuteGroup
            beforeExecuteGroup
                beforeExecuteGroup
                    beforeExecuteTest
                    afterExecuteTest
                    beforeExecuteTest
                    afterExecuteTest
                afterExecuteGroup
                beforeExecuteTest
                afterExecuteTest
            afterExecuteGroup
        afterExecuteSpec
        """.trimIndent()))
    }

    @Test
    fun testSubjectSpekLifeCycleExtensionPointsIsolation() {
        @SimpleExtension
        class AnotherSpec: SubjectSpek<List<String>>({
            group("another nested group") {
                test("another nested test") { }
            }
        })

        class SomeSpek: SubjectSpek<List<String>>({
            subject { emptyList() }
            includeSubjectSpec(AnotherSpec::class)
            group("some group") {
                group("another group") {
                    test("test") { }
                    test("another test") { }
                }

                test("yet another test") { }
            }
        })

        SpekSimpleExtension.builder = StringBuilder()

        executeTestsForClass(SomeSpek::class)

        assertThat(SpekSimpleExtension.builder.trim().toString(), equalTo("""
        beforeExecuteGroup
            beforeExecuteGroup
                beforeExecuteTest
                afterExecuteTest
            afterExecuteGroup
        afterExecuteGroup
        """.trimIndent()))
    }
}
