package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class FixturesActionOrderTest: AbstractSpekRuntimeTest() {
    @Test
    fun testBeforeEachTestOrder() {
        buffer.setLength(0)

        class TestSpek: Spek({
            beforeEachTest { buffer.appendln("1") }
            beforeEachTest { buffer.appendln("2") }

            group("some group") {
                beforeEachTest { buffer.appendln("3") }
                beforeEachTest { buffer.appendln("4") }

                action("some action") {
                    test("do something") { }
                    test("do another thing") { }
                }
            }
        })

        executeTestsForClass(TestSpek::class)
        val expected = """
        1
        2
        3
        4
        """.trimIndent()

        assertThat(buffer.toString().trimIndent(), equalTo(expected))
    }

    @Test
    fun testAfterEachTestOrder() {
        buffer.setLength(0)

        class TestSpek: Spek({
            afterEachTest { buffer.appendln("1") }
            afterEachTest { buffer.appendln("2") }

            group("some group") {
                afterEachTest { buffer.appendln("3") }
                afterEachTest { buffer.appendln("4") }

                action("some action") {
                    test("do something") { }
                    test("do another thing") { }
                }
            }
        })

        executeTestsForClass(TestSpek::class)
        val expected = """
        4
        3
        2
        1
        """.trimIndent()

        assertThat(buffer.toString().trimIndent(), equalTo(expected))
    }

    @Test
    fun testBeforeGroupOrder() {
        buffer.setLength(0)

        class TestSpek: Spek({
            beforeGroup { buffer.appendln("1") }
            beforeGroup { buffer.appendln("2") }

            group("some group") {
                beforeGroup { buffer.appendln("3") }
                beforeGroup { buffer.appendln("4") }

                action("some action") {
                    test("do something") { }
                    test("do another thing") { }
                }
            }
        })

        executeTestsForClass(TestSpek::class)
        val expected = """
        1
        2
        3
        4
        """.trimIndent()

        assertThat(buffer.toString().trimIndent(), equalTo(expected))
    }

    @Test
    fun testAfterGroupOrder() {
        buffer.setLength(0)

        class TestSpek: Spek({
            afterGroup { buffer.appendln("1") }
            afterGroup { buffer.appendln("2") }

            group("some group") {
                afterGroup { buffer.appendln("3") }
                afterGroup { buffer.appendln("4") }

                action("some action") {
                    test("do something") { }
                    test("do another thing") { }
                }
            }
        })

        executeTestsForClass(TestSpek::class)
        val expected = """
        4
        3
        2
        1
        """.trimIndent()

        assertThat(buffer.toString().trimIndent(), equalTo(expected))
    }

    companion object {
        val buffer = StringBuilder()
    }
}
