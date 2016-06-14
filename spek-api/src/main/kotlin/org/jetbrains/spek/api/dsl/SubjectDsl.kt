package org.jetbrains.spek.api.dsl

import org.jetbrains.spek.api.subject.Subject

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
interface SubjectDsl<T>: Dsl {
    fun subject(factory: () -> T): Subject<T>
    val subject: T
}
