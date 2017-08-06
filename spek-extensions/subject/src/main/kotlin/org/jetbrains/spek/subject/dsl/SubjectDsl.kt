package org.jetbrains.spek.subject.dsl

import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface SubjectDsl<T>: Spec {
    val subject: T

    fun subject(): LifecycleAware<T>
}
