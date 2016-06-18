package org.jetbrains.spek.api.dsl

import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.api.memoized.Subject
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
interface SubjectDsl<T>: Dsl {
    fun subject(mode: CachingMode = CachingMode.TEST, factory: () -> T): Subject<T>
    val subject: T

    fun <T, K: SubjectSpek<T>> includeSubjectSpec(spec: KClass<K>)
}
