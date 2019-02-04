package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.utils.Base64
import org.spekframework.spek2.runtime.utils.toByteArray
import kotlin.reflect.KClass

actual data class Path(actual val name: String, actual val parent: Path?) {
    private val serialized by lazy {
        serialize(this)
    }

    private val humanReadable by lazy {
        serialize(this, false)
    }

    private val encoded by lazy {
        encode(name)
    }

    actual fun resolve(name: String) = Path(name, this)

    actual fun isParentOf(path: Path): Boolean {
        var current: Path? = path

        while (current != null) {
            if (current == this) {
                return true
            }
            current = current.parent
        }

        return false
    }

    actual fun serialize(): String = serialized

    override fun toString(): String {
        return humanReadable
    }

    companion object {
        const val PATH_SEPARATOR = '/'

        private fun serialize(path: Path, encoded: Boolean = true): String {
            return if (path.parent == null) {
                // this will be an empty string
                path.name
            } else {
                val name = if (encoded) {
                    path.encoded
                } else {
                    path.name
                }
                "${serialize(path.parent, encoded)}$PATH_SEPARATOR$name".trimStart(PATH_SEPARATOR)
            }
        }

        fun encode(name: String): String {
            return Base64.encodeString(name)
        }

        fun decode(name: String): String {
            return Base64.decodeString(name)
        }
    }
}


actual class PathBuilder(private var parent: Path) {
    constructor() : this(ROOT)

    actual fun append(name: String): PathBuilder {
        parent = Path(name, parent)
        return this
    }

    actual fun build(): Path = parent

    actual companion object {
        actual val ROOT: Path = Path("", null)

        actual fun from(clz: KClass<out Spek>): PathBuilder {
            val name = clz.qualifiedName

            if (name == null) {
                throw UnsupportedOperationException("Can't create a path from $clz")
            }

            return PathBuilder()
                    .append(name)
        }

        actual fun parse(path: String): PathBuilder {
            var builder = PathBuilder()

            path.split(Path.PATH_SEPARATOR)
                    .filter { it.isNotBlank() }
                    .forEach { builder = builder.append(Path.decode(it)) }

            return builder

        }
    }
}
