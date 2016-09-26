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

@Experimental
infix fun <T, K: T> SubjectDsl<K>.itBehavesLike(spec: SubjectSpek<T>) {
    include(Spek.wrap {
        val value: SubjectProviderDsl<T> = object: SubjectProviderDsl<T>, Spec by this {
            val adapter = object: LifecycleAware<T> {
                override fun getValue(thisRef: LifecycleAware<T>, property: KProperty<*>): T {
                    return this()
                }

                override fun invoke(): T {
                    return this@itBehavesLike.subject().invoke()
                }

            }

            override fun subject() = adapter
            override fun subject(mode: CachingMode, factory: () -> T) = adapter
            override val subject: T
                get() = adapter()

        }
        spec.spec(value)
    })
}
