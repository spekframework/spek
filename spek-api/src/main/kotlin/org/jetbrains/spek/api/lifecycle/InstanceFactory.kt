package org.jetbrains.spek.api.lifecycle

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.meta.Experimental
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Experimental
interface InstanceFactory {
    fun <T: Spek> create(spek: KClass<T>): T
}
