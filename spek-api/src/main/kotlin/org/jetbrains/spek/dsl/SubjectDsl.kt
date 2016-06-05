package org.jetbrains.spek.dsl

import org.jetbrains.spek.subject.Subject

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
interface SubjectDsl<T>: Dsl {
    fun subject(factory: () -> T): Subject<T>
    val subject: T
}
