package org.jetbrains.spek.extension.execution

import org.jetbrains.spek.extension.Extension
import org.jetbrains.spek.extension.GroupExtensionContext
import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface BeforeExecuteGroup: Extension {
    fun beforeExecuteGroup(group: GroupExtensionContext)
}
