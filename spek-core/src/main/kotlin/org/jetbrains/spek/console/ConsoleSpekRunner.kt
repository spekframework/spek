package org.jetbrains.spek.console

import org.jetbrains.spek.api.Notifier
import org.jetbrains.spek.api.Spek


class ConsoleSpekRunner(val notifier: ConsoleNotifier) {
    fun runSpecs(paths: List<String>, packageName: String) {
        run(findSpecs(paths, packageName))
        notifier.finish()
    }

    private fun run(classes: List<Spek>) {
        classes.forEach {
            executeSpek(it, notifier)
        }
    }
}

fun executeSpek(spek: Spek, notifier: Notifier) {
    spek.run(notifier);
}