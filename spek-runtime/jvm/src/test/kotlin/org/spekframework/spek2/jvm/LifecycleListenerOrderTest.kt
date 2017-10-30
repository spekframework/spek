package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class LifecycleListenerOrderTest: AbstractSpekRuntimeTest() {

    @Test
    fun testBeforeExecuteTest() {
        buffer.setLength(0)

        class TestSpek: Spek({
            registerListener(object: LifecycleListener {
                override fun beforeExecuteTest(test: TestScope) {
                    buffer.appendln("1")
                }
            })

            beforeEachTest { buffer.appendln("2") }

            group("some group") {
                beforeEachTest { buffer.appendln("3") }

                test("do something") { }
                test("do another thing") { }
            }
        })

        executeTestsForClass(TestSpek::class)
        val expected = """
        1
        2
        3
        1
        2
        3
        """.trimIndent()

        assertThat(buffer.toString().trimIndent(), equalTo(expected))
    }

    @Test
    fun testAfterExecuteTest() {
        buffer.setLength(0)

        class TestSpek: Spek({
            registerListener(object: LifecycleListener {
                override fun afterExecuteTest(test: TestScope) {
                    buffer.appendln("1")
                }
            })

            afterEachTest { buffer.appendln("2") }

            group("some group") {
                afterEachTest { buffer.appendln("3") }

                test("do something") { }
                test("do another thing") { }
            }
        })

        executeTestsForClass(TestSpek::class)
        // afterXXX fixtures are executed from bottom to top
        val expected = """
        3
        2
        1
        3
        2
        1
        """.trimIndent()

        assertThat(buffer.toString().trimIndent(), equalTo(expected))
    }

    @Test
    fun testBeforeExecuteGroup() {
        buffer.setLength(0)

        class TestSpek: Spek({
            registerListener(object: LifecycleListener {
                override fun beforeExecuteGroup(group: GroupScope) {
                    buffer.appendln("1")
                }
            })

            beforeGroup { buffer.appendln("2") }

            group("some group") {
                beforeGroup { buffer.appendln("3") }

                test("do something") { }
                test("do another thing") { }
            }
        })

        executeTestsForClass(TestSpek::class)
        val expected = """
        1
        2
        1
        3
        """.trimIndent()

        assertThat(buffer.toString().trimIndent(), equalTo(expected))
    }

    @Test
    fun testAfterExecuteGroup() {
        buffer.setLength(0)

        class TestSpek: Spek({
            registerListener(object: LifecycleListener {
                override fun afterExecuteGroup(group: GroupScope) {
                    buffer.appendln("1")
                }
            })

            afterGroup { buffer.appendln("2") }

            group("some group") {
                afterGroup { buffer.appendln("3") }

                test("do something") { }
                test("do another thing") { }
            }
        })

        executeTestsForClass(TestSpek::class)
        val expected = """
        3
        1
        2
        1
        """.trimIndent()

        assertThat(buffer.toString().trimIndent(), equalTo(expected))
    }

    companion object {
        val buffer = StringBuilder()
    }
}
