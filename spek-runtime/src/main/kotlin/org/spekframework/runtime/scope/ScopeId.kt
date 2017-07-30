package org.spekframework.runtime.scope

enum class ScopeType {
    CLASS,
    SCOPE;

    override fun toString() = name.toLowerCase()
}

data class ScopeId(val type: ScopeType, val name: String)
