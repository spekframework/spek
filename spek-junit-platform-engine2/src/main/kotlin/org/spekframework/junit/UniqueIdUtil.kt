package org.spekframework.junit

import org.junit.platform.engine.UniqueId
import org.spekframework.runtime.scope.Path
import org.spekframework.runtime.scope.isRoot

val SPEK_ENGINE_UID = UniqueId.forEngine("spek")

fun toUniqueId(path: Path): UniqueId {
    return if (path.isRoot) {
        SPEK_ENGINE_UID
    } else {
        toUniqueId(path.parent!!).append("scope", path.name)
    }
}
