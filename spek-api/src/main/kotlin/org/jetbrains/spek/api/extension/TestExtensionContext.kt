package org.jetbrains.spek.api.extension

import org.jetbrains.spek.api.annotation.Beta

/**
 * @author Ranie Jade Ramiso
 */
@Beta
interface TestExtensionContext: ExtensionContext {
    val parent: GroupExtensionContext
}
