package org.jetbrains.spek.engine

/**
 * @author Ranie Jade Ramiso
 */
class ThrowableCollector {
    private var throwable: Throwable? = null

    fun add(throwable: Throwable) {
        if (this.throwable == null) {
            this.throwable = throwable
        } else {
            (this.throwable as java.lang.Throwable).addSuppressed(throwable)
        }
    }


    inline fun executeSafely(block: () -> Unit) {
        try {
            block.invoke()
        } catch (e: Throwable) {
            add(e)
        }
    }

    fun isEmpty() = throwable == null

    fun assertEmpty() {
        throwable?.let {
            if (it !is AssertionError) it.printStackTrace()
            throw it
        }
    }
}
