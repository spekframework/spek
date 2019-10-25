package org.spekframework.spek2.lifecycle

enum class ScopeType {
    Class,
    Scope;

    override fun toString() = name.toLowerCase()
}

data class ScopeId(val type: ScopeType, val name: String)
