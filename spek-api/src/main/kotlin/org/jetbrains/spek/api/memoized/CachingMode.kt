package org.jetbrains.spek.api.memoized

/**
 * Specifies how [subjects][Subject] are cached.
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
enum class CachingMode {
    /**
     * Subjects will be shared throughout the group which it was declared.
     */
    GROUP,
    /**
     * Each test will get their own unique instance.
     */
    TEST
}
