package org.jetbrains.spek.api

interface TestAction {
    fun description(): String
    fun run(notifier: Notifier)
    fun type(): ActionType

    fun recordedActions(): List<TestAction>
}

enum class ActionType {
    IT, DESCRIBE
}