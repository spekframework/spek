package org.jetbrains.spek.api

interface SpekNodeRunner {
    fun run(tree: SpekTree, notifier: Notifier, innerAction: () -> Unit)
}

class SpekStepRunner(val befores: List<() -> Unit> = emptyList(),
                     val afters: List<() -> Unit> = emptyList(),
                     val assertions: () -> Unit = {}): SpekNodeRunner {

    override fun run(tree: SpekTree, notifier: Notifier, innerAction: () -> Unit) {
        notifier.start(tree)
        try {
            befores.forEach { it() }
            assertions()
            innerAction()
            afters.forEach { it() }
            notifier.succeed(tree)
        } catch(e: Throwable) {
            notifier.fail(tree, e)
        }
    }


}

class SpekIgnoreRunner() : SpekNodeRunner {
    override fun run(tree: SpekTree, notifier: Notifier, innerAction: () -> Unit) {
        notifier.ignore(tree)
    }

}
