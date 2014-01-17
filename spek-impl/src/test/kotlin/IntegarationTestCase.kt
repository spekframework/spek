package org.spek.impl

import kotlin.test.assertEquals
import org.spek.impl.Runner
import org.spek.impl.TestFixtureAction
import org.spek.impl.StepListener
import org.spek.impl.events.Listener


public open class IntegrationTestCase {

    fun runTest(case: TestFixtureAction, vararg expected: String) {
        val list = arrayListOf<String>()
        Runner.executeSpek(case, TestLogger(list))
        if (expected.size == 0) return
        assertEquals(expected.map { it.trim()} . filter {it.length > 0} .toList(), list)
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

        override fun spek(spek: String): StepListener = step("SPEK:"+ spek)
        override fun given(given: String): StepListener = step("GIVEN:" + given)
        override fun on(given: String, on: String)= step("GIVEN:" + given + " ON:" + on)
        override fun it(given: String, on: String, it: String) = step("GIVEN:" + given + " ON:" + on + " IT:" + it)
    }

}