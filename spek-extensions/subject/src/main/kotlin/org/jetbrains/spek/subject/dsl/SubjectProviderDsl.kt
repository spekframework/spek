package org.jetbrains.spek.subject.dsl

import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface SubjectProviderDsl<T>: SubjectDsl<T> {
    fun subject(mode: CachingMode = CachingMode.TEST, factory: () -> T): LifecycleAware<T>
}
