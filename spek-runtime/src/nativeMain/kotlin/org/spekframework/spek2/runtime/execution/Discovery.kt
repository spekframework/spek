package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import kotlin.reflect.KClass

typealias SpekProducer = () -> Spek

data class DiscoveryContext(val classes: Map<KClass<out Spek>, SpekProducer>) {
    companion object {
        fun builder(): DiscoveryContextBuilder = DiscoveryContextBuilder()
    }
}

class DiscoveryContextBuilder {
    private val classes = mutableMapOf<KClass<out Spek>, SpekProducer>()

    fun <T : Spek> addClass(klass: KClass<T>, producer: () -> T): DiscoveryContextBuilder {
        classes.put(klass, producer)
        return this
    }

    fun build(): DiscoveryContext = DiscoveryContext(classes)
}

inline fun <reified T : Spek> DiscoveryContextBuilder.addClass(noinline producer: () -> T): DiscoveryContextBuilder = this.addClass(T::class, producer)

actual data class DiscoveryRequest(val context: DiscoveryContext, val paths: Set<Path> = setOf(PathBuilder.ROOT))
