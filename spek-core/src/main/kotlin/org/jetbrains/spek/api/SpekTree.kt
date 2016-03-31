package org.jetbrains.spek.api

open class SpekTree(val description: String,
                    val type: ActionType,
                    val runner: SpekNodeRunner,
                    val children: List<SpekTree>,
                    private val focused: Boolean = false) {

    fun run(notifier: Notifier) {
        val allPaths = getPaths()
        val focusedPaths = allPaths.filter { it.focused }

        val pathsToRun = if(focusedPaths.size > 0) focusedPaths else allPaths.toList()

        pathsToRun.forEach { path ->
            runPath(path, notifier)
        }
    }

    private fun runPath(path: Path, notifier: Notifier) {
        runner.run(this, notifier) {
            if (path.size > 0) {
                val newPath = path.subPath()
                val child = children.get(path[0])
                child.runPath(newPath, notifier)
            }
        }
    }

    private fun getPaths(): Set<Path> {
        val result: MutableSet<Path> = mutableSetOf()
        if (children.size > 0) {
            children.forEachIndexed { i, child ->
                child.getPaths().forEach { path ->
                    result.add(path.append(i, focused))
                }
            }
        } else {
            result.add(Path(listOf(), focused))
        }
        return result
    }

    fun containsFocusedNodes(): Boolean {
        if (focused) {
            return true
        }

        return children.any { it.containsFocusedNodes() }
    }
}

enum class ActionType {
    IT, DESCRIBE
}


