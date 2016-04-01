package org.jetbrains.spek.api.integration

import org.jetbrains.spek.api.DescribeBody
import org.jetbrains.spek.api.Notifier
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SpekTree
import org.jetbrains.spek.console.executeSpek
import org.junit.Assert

open class IntegrationTestCase {

    fun runTest(case: Spek, vararg expected: String) {
        val list = arrayListOf<String>()
        executeSpek(case, TestLogger(list))
        if (expected.size == 0) return
        val actualDump = list.map { it + "\n" }.fold("") { r, i -> r + i }
        val expectedLog = expected
                .flatMap { it
                    .trim()
                    .split("[\r\n]+".toRegex())
                    .map { it.trim() }
                    .filter { it.length > 0 }
        } . filter { it.length > 0 }  . toList()

        Assert.assertEquals(
                actualDump,
                expectedLog,
                list
                )
    }

    fun data(f: DescribeBody.() -> Unit) : Spek {
        return Spek(f)
    }

    class TestLogger(val output: MutableList<String>): Notifier {
        override fun start(key: SpekTree) {
            output.add(key.description + ": START")
        }

        override fun succeed(key: SpekTree) {
            output.add(key.description + ": FINISH")
        }

        override fun fail(key: SpekTree, error: Throwable) {
            output.add(key.description + ": FAIL: " + error.message)
        }

        override fun ignore(key: SpekTree) {
            output.add(key.description + ": IGNORE")
        }
    }

}