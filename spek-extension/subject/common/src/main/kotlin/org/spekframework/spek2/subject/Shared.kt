package org.spekframework.spek2.subject

import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Spec
import org.spekframework.spek2.include
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.meta.Experimental
import org.spekframework.spek2.subject.dsl.SubjectDsl
import org.spekframework.spek2.subject.dsl.SubjectProviderDsl
import kotlin.reflect.KProperty

internal class IncludedSubjectSpek<T>(val adapter: LifecycleAware<T>, spec: Spec): SubjectProviderDsl<T>, Spec by spec {
    override fun subject() = adapter
    override fun subject(mode: CachingMode, factory: () -> T): LifecycleAware<T> {
        return adapter
    }
    override val subject: T
        get() = adapter()
}

@Experimental
infix fun <T, K: T> SubjectDsl<K>.itBehavesLike(spec: SubjectSpek<T>) {
    include(Spek.wrap {
        val adapter = object: LifecycleAware<T> {
            override fun getValue(thisRef: Any?, property: KProperty<*>): T {
                return this()
            }

            override fun invoke(): T {
                return this@itBehavesLike.subject().invoke()
            }

        }
        spec.spec.invoke(IncludedSubjectSpek(adapter, this))
    })

}
