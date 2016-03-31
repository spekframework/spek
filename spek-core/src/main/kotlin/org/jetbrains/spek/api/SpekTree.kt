package org.jetbrains.spek.api

open class SpekTree(val description: String,
                    val type: ActionType,
                    val runner: SpekTreeRunner,
                    val children: List<SpekTree>,
                    private val _focused: Boolean = false) {

    fun runPath(path: Path, notifier: Notifier) {
        runner.run(this, notifier) {
            if (path.size > 0) {
                val newPath = path.subPath()
                val child = children.get(path[0])
                child.runPath(newPath, notifier)
            }
        }
    }

    fun getPaths(): Set<Path> {
        val result: MutableSet<Path> = mutableSetOf()
        if (children.size > 0) {
            children.forEachIndexed { i, child ->
                child.getPaths().forEach { path ->
                    result.add(path.append(i, _focused))
                }
            }
        } else {
            result.add(Path(listOf(), _focused))
        }
        return result
    }

    fun focused(): Boolean {
        if (_focused) {
            return true
        }

        return children.any { it.focused() }
    }
}

enum class ActionType {
    IT, DESCRIBE
}

class Path(val indexPath: List<Int>,
           val focused: Boolean) : List<Int> by indexPath {
    fun append(i: Int, nodeFocused: Boolean) : Path {
        return Path(listOf(i) + indexPath, focused || nodeFocused)
    }

    fun subPath() : Path {
        return Path(indexPath.subList(1, indexPath.size), focused)
    }

    override fun toString() : String {
        return "[${if(focused) "focused" else "unfocused"}:${indexPath.joinToString()}]"
    }

    override fun equals(other: Any?): Boolean {
        if(other is Path) {
            return indexPath.equals(other.indexPath) && (focused == other.focused)
        }
        return false
    }

    override fun hashCode() : Int {
        return indexPath.hashCode()
    }
}

