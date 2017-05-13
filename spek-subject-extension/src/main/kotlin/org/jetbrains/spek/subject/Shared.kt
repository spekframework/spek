package org.jetbrains.spek.subject

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.api.include
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.meta.Experimental
import org.jetbrains.spek.subject.dsl.SubjectDsl
import org.jetbrains.spek.subject.dsl.SubjectProviderDsl
import kotlin.reflect.KProperty

class IncludedSubjectSpek<T>(val adapter: LifecycleAware<T>, spec: Spec): SubjectProviderDsl<T>, Spec by spec {
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
