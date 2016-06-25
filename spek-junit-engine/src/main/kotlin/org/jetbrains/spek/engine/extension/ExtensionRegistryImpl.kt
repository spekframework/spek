package org.jetbrains.spek.engine.extension

import org.jetbrains.spek.api.extension.Extension
import org.jetbrains.spek.api.extension.ExtensionRegistry
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
class ExtensionRegistryImpl: ExtensionRegistry {
    private val extensions: MutableSet<Extension> = HashSet()

    override fun <T: Extension> getExtension(extension: KClass<T>): T? {
        return extensions().filterIsInstance(extension.java)
            .firstOrNull()
    }

    fun registerExtension(extension: Extension) {
        extensions.add(extension)
    }

    fun extensions(): Sequence<Extension> = extensions.asSequence()
}
