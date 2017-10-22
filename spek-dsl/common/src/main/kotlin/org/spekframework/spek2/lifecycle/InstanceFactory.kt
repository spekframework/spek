package org.spekframework.spek2.lifecycle

import org.spekframework.spek2.Spek
import org.spekframework.spek2.meta.Experimental
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Experimental
interface InstanceFactory {
    fun <T: Spek> create(spek: KClass<T>): T
}
