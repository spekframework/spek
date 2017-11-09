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
 * @property type type of scope.
 * @property prefix prefix appended to the description (if applicable), this will appear in the test report.
 * @property excluded whether the synonym represents an ignored scope.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class Synonym(val type: SynonymType,
                         val prefix: String = "",
                         val excluded: Boolean = false)

/**
 * Controls how descriptions are derived by the IDE.
 *
 * @property desc the description, only applies if annotation is used on a function.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.BINARY)
annotation class Description(val desc: String = "")
