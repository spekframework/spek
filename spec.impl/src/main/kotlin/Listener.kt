package org.spek.impl.events

import org.spek.impl.*

public trait Listener {
    fun spek(spek: String): StepListener
    fun given(given: String): StepListener
    fun on(given: String, on: String): StepListener
    fun it(given: String, on: String, it: String): StepListener
}


public class Multicaster: Listener {
    private val listeners = arrayListOf<Listener>()

    public fun addListener(l: Listener) {
        listeners add l
    }

    override fun spek(spek: String): StepListener {
        return StepMulticaster(listeners.map{ it.spek(spek) })
    }

    override fun given(given: String): StepListener {
        return StepMulticaster(listeners.map{ it.given(given) })
    }
    override fun on(given: String, on: String): StepListener {
        return StepMulticaster(listeners.map{ it.on(given, on) })
    }
    override fun it(given: String, on: String, it: String): StepListener {
        return StepMulticaster(listeners.map{ iit -> iit.it(given, on, it) })
    }
}

public class StepMulticaster(val listeners: List<StepListener>): StepListener {
    override fun executionStarted() {
        listeners forEach { it.executionStarted() }
    }
    override fun executionCompleted() {
        listeners forEach { it.executionCompleted() }
    }
    override fun executionSkipped(why: String) {
        listeners forEach { it.executionSkipped(why) }
    }
    override fun executionPending(why: String) {
        listeners forEach { it.executionPending(why) }
    }
    override fun executionFailed(error: Throwable) {
        listeners forEach { it.executionFailed(error) }
    }
}

