package org.spek.impl

import kotlin.test.assertEquals
import org.spek.impl.Runner
import org.spek.impl.TestFixtureAction
import org.spek.impl.StepListener
import org.spek.impl.events.Listener
import org.spek.api.Specification
import org.junit.Assert


public open class IntegrationTestCase {

    fun runTest(case: TestFixtureAction, vararg expected: String) {
        val list = arrayListOf<String>()
        Runner.executeSpek(case, TestLogger(list))
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

    public fun data(f: Specification.() -> Unit) : TestFixtureAction {
        val d = Data()
        d.f()
        return d
    }

    public open class Data : SpekImpl(), TestFixtureAction {
        override fun description(): String = "42"
    }

    public class TestLogger(val output: MutableList<String>): Listener {
        private fun step(prefix:String) : StepListener = object : StepListener {
            override fun executionStarted() {
                output add prefix + " START"
            }
            override fun executionCompleted() {
                output add prefix + " FINISH"
            }
            override fun executionSkipped(why: String) {
                output add prefix + " SKIP:" + why
            }
            override fun executionPending(why: String) {
                output add prefix + " PEND:" + why
            }
            override fun executionFailed(error: Throwable) {
                output add prefix + " FAIL:" + error.getMessage()
            }
        }

        override fun spek(spek: String): StepListener = step("SPEK: $spek")
        override fun given(spek: String, given: String): StepListener = step("SPEK: $spek GIVEN: $given")
        override fun on(spek: String, given: String, on: String)= step("SPEK: $spek GIVEN: $given ON: $on")
        override fun it(spek: String, given: String, on: String, it: String) = step("SPEK: $spek GIVEN: $given ON: $on IT: $it")
    }

}