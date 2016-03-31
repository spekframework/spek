package org.jetbrains.spek.api

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
