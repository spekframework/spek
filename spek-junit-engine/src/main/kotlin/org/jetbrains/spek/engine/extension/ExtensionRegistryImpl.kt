package org.jetbrains.spek.engine.extension

import org.jetbrains.spek.extension.Extension
import org.jetbrains.spek.extension.ExtensionRegistry
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
class ExtensionRegistryImpl: ExtensionRegistry {
    private val extensions: MutableMap<KClass<*>, Extension> = HashMap()

    override fun <T: Extension> getExtension(extension: KClass<T>): T? {
        return extensions[extension] as T?
    }

    fun <T: Extension> registerExtension(extension: T) {
        if (!extensions.containsKey(extension.javaClass.kotlin)) {
            extensions.put(extension.javaClass.kotlin, extension)
        }
    }

    fun extensions(): Sequence<Extension> = extensions.values.asSequence()
}
