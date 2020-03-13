package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.meta.*
import org.spekframework.spek2.Spek

sealed class Skip {
    class Yes(val reason: String? = null) : Skip()
    object No : Skip()
}

@SpekDsl
interface Root : GroupBody {
    fun registerListener(listener: LifecycleListener)
    fun include(spek: Spek) = spek.root(this)
}

@SpekDsl
interface GroupBody : LifecycleAware, TestContainer {
    @Synonym(type = SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun group(description: String, skip: Skip = Skip.No, defaultCachingMode: CachingMode = CachingMode.INHERIT, preserveExecutionOrder: Boolean = false, failFast: Boolean = false, body: GroupBody.() -> Unit)
}

typealias Fixture = suspend () -> Unit

@SpekDsl
interface LifecycleAware : ScopeBody {
    val defaultCachingMode: CachingMode

    fun <T> memoized(mode: CachingMode = defaultCachingMode, factory: suspend () -> T): MemoizedValue<T>
    fun <T> memoized(mode: CachingMode = defaultCachingMode, factory: suspend () -> T, destructor: suspend (T) -> Unit): MemoizedValue<T>

    fun beforeEachTest(fixture: Fixture)
    fun afterEachTest(fixture: Fixture)

    fun beforeEachGroup(fixture: Fixture)
    fun afterEachGroup(fixture: Fixture)

    fun beforeGroup(fixture: Fixture)
    fun afterGroup(fixture: Fixture)
}

interface ScopeBody {
    fun <T> memoized(): MemoizedValue<T>
}

@SpekDsl
interface TestContainer {
    var defaultTimeout: Long

    @Synonym(type = SynonymType.TEST)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun test(description: String, skip: Skip = Skip.No, timeout: Long = defaultTimeout, body: suspend TestBody.() -> Unit)
}

@SpekDsl
interface TestBody : ScopeBody
