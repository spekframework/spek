package org.spek.console

public trait WorkflowReporter {
    fun spek(spek: String): ActionStatusReporter
    fun given(spek: String, given: String): ActionStatusReporter
    fun on(spek: String, given: String, on: String): ActionStatusReporter
    fun it(spek: String, given: String, on: String, it: String): ActionStatusReporter
}


public class CompositeWorkflowReporter : WorkflowReporter {
    private val listeners = arrayListOf<WorkflowReporter>()

    public fun addListener(l: WorkflowReporter) {
        listeners add l
    }

    override fun spek(spek: String): ActionStatusReporter {
        return CompositeActionStatusReporter(listeners.map { it.spek(spek) })
    }

    override fun given(spek: String, given: String): ActionStatusReporter {
        return CompositeActionStatusReporter(listeners.map { it.given(spek, given) })
    }
    override fun on(spek: String, given: String, on: String): ActionStatusReporter {
        return CompositeActionStatusReporter(listeners.map { it.on(spek, given, on) })
    }
    override fun it(spek: String, given: String, on: String, it: String): ActionStatusReporter {
        return CompositeActionStatusReporter(listeners.map { iit -> iit.it(spek, given, on, it) })
    }
}


