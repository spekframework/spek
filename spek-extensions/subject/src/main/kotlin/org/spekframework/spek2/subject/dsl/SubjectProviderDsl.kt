package org.spekframework.spek2.subject.dsl

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface SubjectProviderDsl<T>: SubjectDsl<T> {
    fun subject(mode: CachingMode = CachingMode.TEST, factory: () -> T): LifecycleAware<T>
}
