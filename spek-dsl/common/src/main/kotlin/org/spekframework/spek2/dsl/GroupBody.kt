package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.meta.*

/**
 * @since 1.0
 */
@SpekDsl
interface GroupBody: TestContainer, ScopeBody {
    @Synonym(type = SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun group(description: String, pending: Pending = Pending.No, body: GroupBody.() -> Unit)

    @Synonym(type = SynonymType.ACTION)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun action(description: String, pending: Pending = Pending.No, body: ActionBody.() -> Unit)

    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T): MemoizedValue<T>
    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T, destructor: (T) -> Unit): MemoizedValue<T>

    fun beforeEachTest(callback: () -> Unit)
    fun afterEachTest(callback: () -> Unit)

    fun beforeGroup(callback: () -> Unit)
    fun afterGroup(callback: () -> Unit)
}
