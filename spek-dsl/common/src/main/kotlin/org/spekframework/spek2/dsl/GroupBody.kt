package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.meta.*

@SpekDsl
interface GroupBody : TestContainer, ScopeBody {
    @Synonym(type = SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun group(description: String, skip: Skip = Skip.No, body: GroupBody.() -> Unit)

    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T): MemoizedValue<T>
    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T, destructor: (T) -> Unit): MemoizedValue<T>

    fun beforeEachTest(callback: () -> Unit)
    fun afterEachTest(callback: () -> Unit)

    fun beforeGroup(callback: () -> Unit)
    fun afterGroup(callback: () -> Unit)
}
