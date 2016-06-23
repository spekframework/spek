package org.jetbrains.spek.api
import java.util.*

interface SpekNodeRunner {
    fun run(tree: SpekTree, notifier: Notifier, innerAction: () -> Unit)
}

class SpekAssertionBody : AssertionBody {
    val cleanups = ArrayList<() -> Unit>()
    override fun onCleanup(block: () -> Unit) { cleanups += block }
}

class SpekStepRunner(val befores: LinkedList<AssertionBody.() -> Unit> = LinkedList(),
                     val afters: LinkedList<() -> Unit> = LinkedList(),
                     val assertions: AssertionBody.() -> Unit = {}): SpekNodeRunner {

    override fun run(tree: SpekTree, notifier: Notifier, innerAction: () -> Unit) {
        notifier.start(tree)
        try {
            val body = SpekAssertionBody()
            try {
                befores.forEach { body.it() }
                body.assertions()
                innerAction()
            } catch(e: Throwable) {
                cleanup(afters + body.cleanups, e)
                throw e
            }
            cleanup(afters + body.cleanups)
            notifier.succeed(tree)
        } catch(e: Throwable) {
            notifier.fail(tree, e)
        }
    }

    private fun cleanup(blocks: List<() -> Unit>, bodyException: Throwable? = null) {
        var exception: Exception? = null
        for (block in blocks) {
            try {
                block.invoke()
            } catch (e: Exception) {
                if (bodyException != null) {
// When JDK 7 is officially supported:
//                    bodyException.addSuppressed(e)
                } else if (exception == null) {
                    exception = e
                } else {
// When JDK 7 is officially supported:
//                    exception.addSuppressed(e)
                }
            }
        }
        if (exception != null) {
            throw exception
        }
    }

}

class SpekIgnoreRunner() : SpekNodeRunner {
    override fun run(tree: SpekTree, notifier: Notifier, innerAction: () -> Unit) {
        notifier.ignore(tree)
    }

}
