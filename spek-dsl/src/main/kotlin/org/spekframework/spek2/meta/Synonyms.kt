package org.spekframework.spek2.meta

enum class SynonymType {
    Group,
    Action,
    Test
}

@Target(AnnotationTarget.FUNCTION)
annotation class Synonym(val type: SynonymType,
                         val prefix: String = "",
                         val excluded: Boolean = false)
