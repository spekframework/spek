package org.jetbrains.spek.api

open class SpekTree(val description: String,
                    val type: ActionType,
                    val runner: SpekTreeRunner,
                    val children: List<SpekTree>) {

    fun runPath(path: List<Int>, notifier: Notifier) {
        runner.run(this, notifier) {
            if (path.size > 0) {
                val newPath = path.subList(1, path.size)
                val child = children.get(path[0])
                child.runPath(newPath, notifier)
            }
        }
    }

    fun getPaths(): Set<List<Int>> {
        val result: MutableSet<List<Int>> = mutableSetOf()
        if (children.size > 0) {
            children.forEachIndexed { i, child ->
                child.getPaths().forEach { path ->
                    result.add(listOf(i) + path)
                }
            }
        } else {
            result.add(listOf())
        }
        return result
    }
}

enum class ActionType {
    IT, DESCRIBE
}

