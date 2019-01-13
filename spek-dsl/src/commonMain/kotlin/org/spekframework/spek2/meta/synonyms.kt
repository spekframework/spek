package org.spekframework.spek2.meta

/**
 * Type of synonym.
 */
enum class SynonymType {
    GROUP,
    TEST
}

/**
 * Marks a function as a synonym to either a group or test scope.
 *
 * @property type type of scope.
 * @property prefix prefix appended to the description (if applicable), this will appear in the test report.
 * @property excluded whether the synonym represents an ignored scope.
 * @property runnable controls whether this scope is runnable from within the IDE.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Synonym(
    val type: SynonymType,
    val prefix: String = "",
    val excluded: Boolean = false,
    val runnable: Boolean = true
)


/**
 * Controls how descriptions are derived by the IDE.
 *
 * @property sources description sources.
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class Descriptions(vararg val sources: Description)

enum class DescriptionLocation {
    TYPE_PARAMETER,
    VALUE_PARAMETER
}

/**
 * A description source.
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class Description(val location: DescriptionLocation, val index: Int)
