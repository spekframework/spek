package org.spekframework.spek2.meta

/**
 * Type of synonym.
 */
enum class SynonymType {
    Group,
    Action,
    Test
}

/**
 * Marks a function as a synonym to either a group, action or test scope.
 *
 *
 * @property type type of scope.
 * @property prefix prefix appended to the description, if applicable.
 * @property excluded whether the synonym represents an ignored scope.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Synonym(val type: SynonymType,
                         val prefix: String = "",
                         val excluded: Boolean = false)
