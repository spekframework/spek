package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.lifecycle.GroupScope
import org.jetbrains.spek.api.lifecycle.LifecycleListener
import org.jetbrains.spek.api.lifecycle.TestScope
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class LifecycleListenerOrderTest: AbstractSpekTestEngineTest() {

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
        1
        3
        2
        1
        3
        2
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
        1
        3
        1
        2
        """.trimIndent()

        assertThat(buffer.toString().trimIndent(), equalTo(expected))
    }

    companion object {
        val buffer = StringBuilder()
    }
}
