package org.jetbrains.spek.api.memoized

import org.jetbrains.spek.api.annotation.Beta

/**
 * Specifies how [subjects][Subject] are cached.
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Beta
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
