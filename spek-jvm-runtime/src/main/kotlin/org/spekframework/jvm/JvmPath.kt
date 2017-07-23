package org.spekframework.jvm

import org.spekframework.runtime.scope.Path

data class JvmPath private constructor(override val name: String, override val parent: JvmPath?): Path {
    private val serialized by lazy {
        serialize(this)
    }

    override fun resolve(name: String) = JvmPath.create(name, this)

    override fun isParentOf(path: Path): Boolean {
        var current: Path? = path

        while (current != null) {
            if (current == this) {
                return true
            }
            current = current.parent
        }

        return false
    }

    override fun serialize(): String {
        return serialized
    }

    companion object {
        val ROOT = JvmPath("", null)
        private val PATH_SEPARATOR = "/"
        private val PATH_SEPARATOR_REGEX = Regex("(?<=[^\\\\])/")

        fun create(name: String, parent: JvmPath? = null): JvmPath {
            return JvmPath(name.replace(PATH_SEPARATOR, "\\/"), parent)
        }

        /**
         * Assumes path is properly encoded, otherwise use [create]
         */
        fun from(path: String): JvmPath {
            return if (path.isEmpty()) {
                ROOT
            } else {
                path.split(PATH_SEPARATOR_REGEX).fold(ROOT) { parent, name ->
                    JvmPath(name, parent)
                }
            }
        }

        private fun serialize(path: JvmPath): String {
            return if (path.parent == null) {
                path.name
            } else {
                "${serialize(path.parent)}$PATH_SEPARATOR${path.name}".trimStart(*PATH_SEPARATOR.toCharArray())
            }
        }
    }
}
