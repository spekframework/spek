package org.spekframework.spek2.runtime.scope

enum class ScopeType {
    CLASS,
    SCOPE;

    override fun toString() = name.toLowerCase()
}

data class ScopeId(val type: ScopeType, val name: String)
