package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.meta.*

@SpekDsl
interface GroupBody : TestContainer, ScopeBody {
    val defaultCachingMode: CachingMode

    @Synonym(type = SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun group(description: String, skip: Skip = Skip.No, defaultCachingMode: CachingMode = CachingMode.INHERIT, body: GroupBody.() -> Unit)

    fun <T> memoized(mode: CachingMode = defaultCachingMode, factory: () -> T): MemoizedValue<T>
    fun <T> memoized(mode: CachingMode = defaultCachingMode, factory: () -> T, destructor: (T) -> Unit): MemoizedValue<T>

    fun beforeEachTest(callback: () -> Unit)
    fun afterEachTest(callback: () -> Unit)

    fun beforeGroup(callback: () -> Unit)
    fun afterGroup(callback: () -> Unit)
}
