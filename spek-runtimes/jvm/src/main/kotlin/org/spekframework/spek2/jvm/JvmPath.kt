package org.spekframework.spek2.jvm

import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import java.util.Base64
import kotlin.reflect.KClass

private data class JvmPath constructor(override val name: String, override val parent: JvmPath?): Path {
    private val serialized by lazy {
        serialize(this)
    }

    private val encoded by lazy {
        encode(name)
    }

    override fun resolve(name: String) = JvmPath(name, this)

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
        const val PATH_SEPARATOR = "/"

        private fun serialize(path: JvmPath): String {
            return if (path.parent == null) {
                // this will be an empty string
                path.name
            } else {
                "${serialize(path.parent)}$PATH_SEPARATOR${path.encoded}".trimStart(*PATH_SEPARATOR.toCharArray())
            }
        }

        fun encode(name: String): String {
            return Base64.getEncoder()
                .encodeToString(name.toByteArray())
        }

        fun decode(name: String): String {
            return String(
                Base64.getDecoder()
                    .decode(name)
            )
        }
    }
}

class JvmPathBuilder private constructor(val parent: JvmPath): PathBuilder {
    constructor(): this(ROOT as JvmPath)

    override fun append(name: String): PathBuilder {
        return JvmPathBuilder(JvmPath(name, parent))
    }

    override fun build(): Path {
        return parent
    }

    companion object {
        val ROOT: Path = JvmPath("", null)

        fun from(clz: KClass<out Spek>): PathBuilder {
            return JvmPathBuilder()
                .append(clz.java.`package`.name)
                .append(clz.java.simpleName)
        }

        fun parse(path: String): PathBuilder {
            var builder: PathBuilder = JvmPathBuilder()

            path.split(JvmPath.PATH_SEPARATOR)
                .filter { it.isNotBlank() }
                .forEach { builder = builder.append(JvmPath.decode(it)) }

            return builder

        }
    }
}
