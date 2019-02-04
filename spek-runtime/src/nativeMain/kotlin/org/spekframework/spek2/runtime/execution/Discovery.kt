package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.Spek
import kotlin.reflect.KClass

typealias SpekProducer = () -> Spek

actual data class DiscoveryRequest(val classes: Map<KClass<out Spek>, SpekProducer>) {
    companion object {
        fun builder(): DiscoveryRequestBuilder = DiscoveryRequestBuilder()
    }
}

class DiscoveryRequestBuilder {
    private val classes = mutableMapOf<KClass<out Spek>, SpekProducer>()

    fun <T : Spek> addClass(klass: KClass<T>, producer: () -> T): DiscoveryRequestBuilder {
        classes.put(klass, producer)
        return this
    }

    fun build(): DiscoveryRequest = DiscoveryRequest(classes)
}

inline fun <reified T : Spek> DiscoveryRequestBuilder.addClass(noinline producer: () -> T): DiscoveryRequestBuilder = this.addClass(T::class, producer)
