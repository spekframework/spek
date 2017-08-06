package org.jetbrains.spek.subject

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.meta.Experimental
import org.jetbrains.spek.subject.core.SubjectProviderDslImpl
import org.jetbrains.spek.subject.dsl.SubjectProviderDsl

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
abstract class SubjectSpek<T>(val subjectSpec: SubjectProviderDsl<T>.() -> Unit): Spek({
    if (this is IncludedSubjectSpek<*>) {
        subjectSpec.invoke(this as IncludedSubjectSpek<T>)
    } else {
        subjectSpec.invoke(SubjectProviderDslImpl(this))
    }
})
