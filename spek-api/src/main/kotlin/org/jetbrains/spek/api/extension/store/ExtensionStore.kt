package org.jetbrains.spek.api.extension.store

import org.jetbrains.spek.api.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface ExtensionStore {
    fun put(key: Any, value: Any)
    fun <T> get(key: Any): T
}
