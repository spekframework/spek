package org.jetbrains.spek.extension

import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface TestExtensionContext: ExtensionContext {
    val parent: GroupExtensionContext
}
