package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.Spek
import kotlin.reflect.KClass

expect class Path {
    val parent: Path?
    val name: String
    fun resolve(name: String): Path
    fun serialize(): String
    fun isParentOf(path: Path): Boolean
}

val Path.isRoot: Boolean
    get() {
        return name.isEmpty() && parent == null
    }

// handles two cases
//    classToPath        -> discoveryRequest.path
// 1: my.package/MyClass -> my.package/MyClass/description
// 2: my.package/MyClass/description -> my.package/MyClass
fun Path.isRelated(path: Path) = this.isParentOf(path) || path.isParentOf(this)


expect class PathBuilder {
    fun append(name: String): PathBuilder
    fun build(): Path

    companion object {
        fun from(clz: KClass<out Spek>): PathBuilder
        fun parse(path: String): PathBuilder
    }
}
