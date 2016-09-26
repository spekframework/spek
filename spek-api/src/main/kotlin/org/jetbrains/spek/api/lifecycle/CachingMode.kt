package org.jetbrains.spek.api.lifecycle

import org.jetbrains.spek.meta.Experimental

/**
 * Specifies how [lifecycle aware objects][LifecycleAware] are cached.
 *
 * @author Ranie Jade Ramiso
 */
@Experimental
enum class CachingMode {
    /**
     * Instance will be shared within the group it was declared.
     */
    GROUP,
    /**
     * Each test will get their own unique instance.
     */
    TEST
}
