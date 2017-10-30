package org.spekframework.spek2.subject

import org.spekframework.spek2.Spek
import org.spekframework.spek2.meta.Experimental
import org.spekframework.spek2.subject.core.SubjectProviderDslImpl
import org.spekframework.spek2.subject.dsl.SubjectProviderDsl

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
abstract class SubjectSpek<T>(val subjectSpec: SubjectProviderDsl<T>.() -> Unit): Spek({
    if (this is IncludedSubjectSpek<*>) {
        @Suppress("UNCHECKED_CAST")
        subjectSpec.invoke(this as IncludedSubjectSpek<T>)
    } else {
        subjectSpec.invoke(SubjectProviderDslImpl(this))
    }
})
