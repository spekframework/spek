package org.jetbrains.spek.console

import org.jetbrains.spek.api.*

public trait ActionStatusReporter {
    fun started() {
    }
    fun completed() {
    }
    fun skipped(why: String) {
    }
    fun pending(why: String) {
    }
    fun failed(error: Throwable) {
    }
}

public class CompositeActionStatusReporter(val reporters: List<ActionStatusReporter>) : ActionStatusReporter {
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

public fun executeWithReporting<T>(t: T, reporter: ActionStatusReporter, action: T.() -> Unit) {
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

