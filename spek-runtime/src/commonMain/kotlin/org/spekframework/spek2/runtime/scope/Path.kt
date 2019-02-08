package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.runtime.util.Base64
import org.spekframework.spek2.runtime.util.ClassUtil
import kotlin.reflect.KClass

val Path.isRoot: Boolean
    get() {
        return name.isEmpty() && parent == null
    }

// handles two cases
//    classToPath        -> discoveryRequest.path
// 1: my.package/MyClass -> my.package/MyClass/description
// 2: my.package/MyClass/description -> my.package/MyClass
fun Path.intersects(path: Path) = this.isParentOf(path) || path.isParentOf(this)

data class Path(val name: String, val parent: Path?) {
    private val serialized by lazy {
        serialize(this)
    }

    private val humanReadable by lazy {
        serialize(this, false)
    }

    private val encoded by lazy {
        encode(name)
    }

    fun resolve(name: String) = Path(name, this)

    fun isParentOf(path: Path): Boolean {
        var current: Path? = path

        while (current != null) {
            if (current == this) {
                return true
            }
            current = current.parent
        }

        return false
    }

    fun serialize(): String = serialized

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
            return Base64.encodeToString(name)
        }

        fun decode(name: String): String {
            return Base64.decodeToString(name)
        }
    }
}

class PathBuilder(private var parent: Path) {
    constructor() : this(ROOT)

    fun appendPackage(packageName: String): PathBuilder {
        packageName.split('.')
            .forEach { part -> append(part) }
        return this
    }

    fun append(name: String): PathBuilder {
        parent = Path(name, parent)
        return this
    }

    fun build(): Path = parent

    companion object {
        val ROOT: Path = Path("", null)

        fun from(clz: KClass<*>): PathBuilder {
            val (packageName, className) = ClassUtil.extractPackageAndClassNames(clz)
            val builder = PathBuilder()
            if (packageName.isNotEmpty()) {
                builder.appendPackage(packageName)
            }
            return builder
                .append(className)
        }

        fun parse(path: String): PathBuilder {
            var builder = PathBuilder()

            path.split(Path.PATH_SEPARATOR)
                .forEach { builder = builder.append(Path.decode(it)) }

            return builder

        }
    }
}
