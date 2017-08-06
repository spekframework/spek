package org.spekframework.spek2.subject.core

import org.spekframework.spek2.dsl.Spec
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.subject.dsl.SubjectProviderDsl
import kotlin.properties.Delegates

/**
 * @author Ranie Jade Ramiso
 */
internal class SubjectProviderDslImpl<T>(spec: Spec): SubjectDslImpl<T>(spec), SubjectProviderDsl<T> {
    var adapter: LifecycleAware<T> by Delegates.notNull()
    override val subject: T
        get() = adapter()

    override fun subject(): LifecycleAware<T> = adapter

    override fun subject(mode: CachingMode, factory: () -> T): LifecycleAware<T> {
        return memoized(mode, factory).apply {
            adapter = this
        }
    }
}
