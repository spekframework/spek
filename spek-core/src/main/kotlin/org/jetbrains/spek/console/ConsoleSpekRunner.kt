package org.jetbrains.spek.console

import org.jetbrains.spek.api.Notifier
import org.jetbrains.spek.api.Spek


class ConsoleSpekRunner(val notifier: ConsoleNotifier) {
    fun runSpecs(paths: List<String>, packageName: String) {
        run(findSpecs(paths, packageName))
        notifier.finish()
    }

    private fun run(classes: List<Spek>) {
        var speksToRun = classes
        if(classes.any { it.tree.containsFocusedNodes() }) {
            speksToRun = classes.filter { it.tree.containsFocusedNodes() }
        }
        speksToRun.forEach {
            executeSpek(it, notifier)
        }
    }
}

fun executeSpek(spek: Spek, notifier: Notifier) {
    spek.run(notifier);
}