package org.spek.impl

public trait StepListener {
    fun executionStarted() {
    }
    fun executionCompleted() {
    }
    fun executionSkipped(why: String) {
    }
    fun executionPending(why: String) {
    }
    fun executionFailed(error: Throwable) {
    }
}

public object Util {
    public fun safeExecute<T>(t: T, listener: StepListener, action: T.() -> Unit) {
        listener.executionStarted()
        try {
            t.action()
        } catch(e: SkippedException) {
            listener.executionSkipped(e.getMessage()!!)
        } catch(e: PendingException) {
            listener.executionPending(e.getMessage()!!)
        } catch(e: Throwable) {
            listener.executionFailed(e)
        } finally {
            listener.executionCompleted()
        }
    }
}
