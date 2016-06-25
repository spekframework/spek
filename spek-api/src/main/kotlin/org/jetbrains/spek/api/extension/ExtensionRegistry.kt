package org.jetbrains.spek.api.extension

import org.jetbrains.spek.api.meta.Experimental
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface ExtensionRegistry {
    fun <T: Extension> getExtension(extension: KClass<T>): T?
}
