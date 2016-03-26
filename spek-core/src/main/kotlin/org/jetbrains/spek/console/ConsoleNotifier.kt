package org.jetbrains.spek.console

import org.jetbrains.spek.api.Notifier

interface ConsoleNotifier: Notifier {
    fun finish()
}