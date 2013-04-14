package org.spek.impl.events

/*
import kotlin.test.assertEquals
import org.spek.junit.api.JUnitSpek
import org.spek.impl.StepListener


class ListenersTest : JUnitSpek() {{
    given("multicaster"){
        val log = arrayListOf<String>()
        val mc = Multicaster()
        mc addListener MockListener{log add "l1: ${it}"}
        mc addListener MockListener{log add "l2: ${it}"}

        on("given event given+start") {
            log.clear()
            mc.given("GGG").executionStarted()

            it("should call main.listeners") {
                assertEquals(listOf(
                        "l1: given GGG",
                        "l2: given GGG",
                        "l1: given: executionStarted",
                        "l2: given: executionStarted"
                ), log)
            }
        }

        on("given event given+on+completed") {
            log.clear()
            mc.on("GGG", "ZZZ").executionCompleted()

            it("should call main.listeners") {
                assertEquals(listOf(
                        "l1: on GGG -> ZZZ",
                        "l2: on GGG -> ZZZ",
                        "l1: given+on: executionCompleted",
                        "l2: given+on: executionCompleted"
                ), log)
            }
        }

        on("given event given+on+it+error") {
            log.clear()
            mc.it("GGG", "ZZZ", "FFF").executionFailed(RuntimeException("mock"))

            it("should call main.listeners") {
                assertEquals(listOf(
                        "l1: on GGG -> ZZZ -> FFF",
                        "l2: on GGG -> ZZZ -> FFF",
                        "l1: given+on+it: executionFailed(mock)",
                        "l2: given+on+it: executionFailed(mock)"
                ), log)
            }
        }
    }
}
    private class MockStepListener(val log : (String) -> Unit) : StepListener {
        override fun executionStarted() {
            log.invoke("executionStarted")
        }
        override fun executionCompleted() {
            log.invoke("executionCompleted")
        }
        override fun executionFailed(error: Throwable) {
            log.invoke("executionFailed(${error.getMessage()})")
        }
    }

    private class MockListener(val l : (String) -> Unit) : Listener {
        override fun given(given: String): StepListener {
            l.invoke("given ${given}")
            return MockStepListener{ l.invoke("given: ${it}")}
        }
        override fun on(given: String, on: String): StepListener {
            l.invoke("on ${given} -> ${on}")
            return MockStepListener{ l.invoke("given+on: ${it}")}

        }
        override fun it(given: String, on: String, it: String): StepListener {
            l.invoke("on ${given} -> ${on} -> ${it}")
            return MockStepListener{ l.invoke("given+on+it: ${it}")}
        }
    }
}
*/