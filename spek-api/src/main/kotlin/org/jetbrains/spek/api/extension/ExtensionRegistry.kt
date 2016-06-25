package org.jetbrains.spek.api.extension

import org.jetbrains.spek.api.annotation.Beta
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
@Beta
interface ExtensionRegistry {
    fun <T: Extension> getExtension(extension: KClass<T>): T?
}
