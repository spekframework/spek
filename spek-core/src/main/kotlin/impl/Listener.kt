package org.spek.impl

import org.spek.impl.*

public trait Listener {
    fun spek(spek: String): ExecutionReporter
    fun given(spek: String, given: String): ExecutionReporter
    fun on(spek: String, given: String, on: String): ExecutionReporter
    fun it(spek: String, given: String, on: String, it: String): ExecutionReporter
}


public class Multicaster : Listener {
    private val listeners = arrayListOf<Listener>()

    public fun addListener(l: Listener) {
        listeners add l
    }

    override fun spek(spek: String): ExecutionReporter {
        return CompositeExecutionReporter(listeners.map { it.spek(spek) })
    }

    override fun given(spek: String, given: String): ExecutionReporter {
        return CompositeExecutionReporter(listeners.map { it.given(spek, given) })
    }
    override fun on(spek: String, given: String, on: String): ExecutionReporter {
        return CompositeExecutionReporter(listeners.map { it.on(spek, given, on) })
    }
    override fun it(spek: String, given: String, on: String, it: String): ExecutionReporter {
        return CompositeExecutionReporter(listeners.map { iit -> iit.it(spek, given, on, it) })
    }
}


