package org.spekframework.spek2.lifecycle

/**
 * Specifies how [lifecycle aware objects][MemoizedValue] are cached.
 */
enum class CachingMode {
    /**
     * Each group will get their own unique instance. Nested groups will have
     * their own unique instance as well.
     */
    @Deprecated(
        "Use CachingMode.EACH_GROUP instead.",
        replaceWith = ReplaceWith("org.spekframework.spek2.lifecycle.CachingMode.EACH_GROUP")
    )
    GROUP,
    /**
     * Each group will get their own unique instance. Nested groups will have
     * their own unique instance as well.
     */
    EACH_GROUP,

    /**
     * Instance will be shared within the group it was declared.
     */
    SCOPE,

    /**
     * Each test will get their own unique instance.
     */
    TEST,

    INHERIT
}
