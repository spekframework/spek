package org.spek.impl

public trait StepListener {
    fun executionStarted() {}
    fun executionCompleted() {}
    fun executionFailed(error : Throwable) {}
}

public object Util {
    public fun safeExecute<T>(t : T, listener : StepListener, action : T.() -> Unit) {
        listener.executionStarted()
        try {
            t.action()
        } catch(e : Throwable) {
            listener.executionFailed(e)
        } finally {
            listener.executionCompleted()
        }
    }
}
