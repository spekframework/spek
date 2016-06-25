package org.jetbrains.spek.api.extension

import org.jetbrains.spek.api.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface GroupExtensionContext: ExtensionContext {
    val parent: GroupExtensionContext?
}
