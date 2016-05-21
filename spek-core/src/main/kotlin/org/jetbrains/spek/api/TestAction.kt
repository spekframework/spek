package org.jetbrains.spek.api

interface TestAction {
    fun description(): String
    fun run(notifier: Notifier, parentContext: SpekContext)
    fun type(): ActionType

    fun children(): List<TestAction>
}

interface SpekContext {
    fun run(test: () -> Unit)
}

enum class ActionType {
    IT, DESCRIBE
}