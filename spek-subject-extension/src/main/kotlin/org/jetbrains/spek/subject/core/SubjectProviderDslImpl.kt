package org.jetbrains.spek.subject.core

import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.subject.dsl.SubjectProviderDsl
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
