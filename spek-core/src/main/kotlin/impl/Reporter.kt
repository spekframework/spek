package org.spek.impl

import org.spek.api.*

public trait ExecutionReporter {
    fun started() {}
    fun completed() {}
    fun skipped(why: String) {}
    fun pending(why: String) {}
    fun failed(error: Throwable) {}
}

public class CompositeExecutionReporter(val reporters: List<ExecutionReporter>) : ExecutionReporter {
    override fun started() {
        for (reporter in reporters)
            reporter.started()
    }
    override fun completed() {
        for (reporter in reporters)
            reporter.completed()
    }
    override fun skipped(why: String) {
        for (reporter in reporters)
            reporter.skipped(why)
    }
    override fun pending(why: String) {
        for (reporter in reporters)
            reporter.pending(why)
    }
    override fun failed(error: Throwable) {
        for (reporter in reporters)
            reporter.failed(error)
    }
}

public fun executeWithReporting<T>(t: T, reporter: ExecutionReporter, action: T.() -> Unit) {
    reporter.started()
    try {
        t.action()
    } catch(e: SkippedException) {
        reporter.skipped(e.getMessage()!!)
    } catch(e: PendingException) {
        reporter.pending(e.getMessage()!!)
    } catch(e: Throwable) {
        reporter.failed(e)
    } finally {
        reporter.completed()
    }
}

