package org.jetbrains.spek.api.extension

import org.jetbrains.spek.api.annotation.Beta

/**
 * @author Ranie Jade Ramiso
 */
@Beta
interface ExtensionStore {
    fun put(key: Any, value: Any)
    fun <T> get(key: Any): T
}
