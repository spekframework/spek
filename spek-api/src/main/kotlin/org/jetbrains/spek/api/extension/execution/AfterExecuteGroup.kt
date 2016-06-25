package org.jetbrains.spek.api.extension.execution

import org.jetbrains.spek.api.annotation.Beta
import org.jetbrains.spek.api.extension.Extension
import org.jetbrains.spek.api.extension.GroupExtensionContext

/**
 * @author Ranie Jade Ramiso
 */
@Beta
interface AfterExecuteGroup: Extension {
    fun afterExecuteGroup(group: GroupExtensionContext)
}
