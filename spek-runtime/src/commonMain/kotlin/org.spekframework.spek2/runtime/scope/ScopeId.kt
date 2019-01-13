package org.spekframework.spek2.runtime.scope

enum class ScopeType {
    Class,
    Scope;

    override fun toString() = name.toLowerCase()
}

data class ScopeId(val type: ScopeType, val name: String)
