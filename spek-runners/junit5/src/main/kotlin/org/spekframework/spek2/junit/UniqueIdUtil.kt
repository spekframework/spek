package org.spekframework.spek2.junit

import org.junit.platform.engine.UniqueId
import org.spekframework.spek2.runtime.scope.ScopeImpl

val SPEK_ENGINE_UID = UniqueId.forEngine("spek")

fun toUniqueId(scope: ScopeImpl?): UniqueId {
    return if (scope == null) {
        SPEK_ENGINE_UID
    } else {
        toUniqueId(scope.parent as ScopeImpl?).append("${scope.id.type}", scope.id.name)

    }
}
