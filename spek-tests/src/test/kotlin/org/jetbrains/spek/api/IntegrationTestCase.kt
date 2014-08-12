package org.jetbrains.spek.api

import kotlin.test.*
import org.jetbrains.spek.api.*
import org.junit.*
import org.jetbrains.spek.console.executeSpek
import org.jetbrains.spek.console.WorkflowReporter
import org.jetbrains.spek.console.ActionStatusReporter

public open class IntegrationTestCase {

    fun runTest(case: TestSpekAction, vararg expected: String) {
        val list = arrayListOf<String>()
        executeSpek(case, TestLogger(list))
        if (expected.size == 0) return
        val actualDump = list.map { it + "\n" }.fold("") { r, i -> r + i }
        val expectedLog = expected
                .flatMap { it
                    .trim()
                    .split("[\r\n]+")
                    .map { it.trim() }
                    .filter { it.length > 0 }
        } . filter { it.length > 0 }  . toList()

        Assert.assertEquals(
                actualDump,
                expectedLog,
                list
                )
    }

    public fun data(f: Specification.() -> Unit) : TestSpekAction {
        val d = object : Data() {}
        d.f()
        return d
    }

    public abstract class Data : Spek(), TestSpekAction {
        override fun description(): String = "42"
    }

    public class TestLogger(val output: MutableList<String>): WorkflowReporter {
        private fun step(prefix:String) : ActionStatusReporter = object : ActionStatusReporter {
            override fun started() {
                output add prefix + " START"
            }
            override fun completed() {
                output add prefix + " FINISH"
            }
            override fun skipped(why: String) {
                output add prefix + " SKIP:" + why
            }
            override fun pending(why: String) {
                output add prefix + " PEND:" + why
            }
            override fun failed(error: Throwable) {
                output add prefix + " FAIL:" + error.getMessage()
            }
        }

        override fun spek(spek: String): ActionStatusReporter = step("SPEK: $spek")
        override fun given(spek: String, given: String): ActionStatusReporter = step("SPEK: $spek GIVEN: $given")
        override fun on(spek: String, given: String, on: String)= step("SPEK: $spek GIVEN: $given ON: $on")
        override fun it(spek: String, given: String, on: String, it: String) = step("SPEK: $spek GIVEN: $given ON: $on IT: $it")
    }

}