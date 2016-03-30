package org.jetbrains.spek.api
import java.util.*

interface SpekTreeRunner {
    fun run(tree: SpekTree, notifier: Notifier, action: () -> Unit)
}

class SpekStepRunner(val befores: LinkedList<() -> Unit> = LinkedList(),
                     val afters: LinkedList<() -> Unit> = LinkedList(),
                     val assertions: () -> Unit = {}): SpekTreeRunner {

    override fun run(tree: SpekTree, notifier: Notifier, action: () -> Unit) {
        notifier.start(tree)
        try {
            befores.forEach { it() }
            assertions()
            action()
            afters.forEach { it() }
            notifier.succeed(tree)
        } catch(e: Throwable) {
            notifier.fail(tree, e)
        }
    }


}

class SpekIgnoreRunner() : SpekTreeRunner {
    override fun run(tree: SpekTree, notifier: Notifier, action: () -> Unit) {
        notifier.ignore(tree)
    }

}
