package org.spek.impl

public trait StepListener {
    fun executionStarted() {
    }
    fun executionCompleted() {
    }
    fun executionSkipped(why: String) {
    }
    fun executionFailed(error: Throwable) {
    }
}

public object Util {
    public fun safeExecute<T>(t: T, listener: StepListener, action: T.() -> Unit) {
        if (t is SkipAction) {
            listener.executionSkipped(t.why())
        } else {
            listener.executionStarted()
            try {
                t.action()
            } catch(e: Throwable) {
                listener.executionFailed(e)
            } finally {
                listener.executionCompleted()
            }
        }
    }
}
