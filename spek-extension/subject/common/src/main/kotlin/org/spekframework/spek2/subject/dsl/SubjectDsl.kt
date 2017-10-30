package org.spekframework.spek2.subject.dsl

import org.spekframework.spek2.dsl.Spec
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface SubjectDsl<T>: Spec {
    val subject: T

    fun subject(): LifecycleAware<T>
}
