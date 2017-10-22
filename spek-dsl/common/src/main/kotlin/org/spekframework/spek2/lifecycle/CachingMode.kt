package org.spekframework.spek2.lifecycle

import org.spekframework.spek2.meta.Experimental

/**
 * Specifies how [lifecycle aware objects][LifecycleAware] are cached.
 *
 * @author Ranie Jade Ramiso
 */
@Experimental
enum class CachingMode {
    /**
     * Each group will get their own unique instance. Nested groups will have
     * their own unique instance as well.
     */
    GROUP,

    /**
     * Instance will be shared within the group it was declared.
     */
    SCOPE,

    /**
     * Each test will get their own unique instance.
     */
    TEST
}
