package org.spekframework.junit

import org.junit.platform.engine.UniqueId
import org.spekframework.runtime.scope.ScopeImpl

val SPEK_ENGINE_UID = UniqueId.forEngine("spek")

fun toUniqueId(scope: ScopeImpl?): UniqueId {
    return if (scope == null) {
        SPEK_ENGINE_UID
    } else {
        toUniqueId(scope.parent as ScopeImpl?).append("${scope.id.type}", scope.id.name)

    }
}
