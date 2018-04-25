package org.spekframework.spek2.subject.dsl

import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.meta.Experimental

@Experimental
interface SubjectDsl<T>: Root {
    @Deprecated("Use returned value, e.g val subject by subject { ... }")
    val subject: T

    fun subject(): LifecycleAware<T>
}
