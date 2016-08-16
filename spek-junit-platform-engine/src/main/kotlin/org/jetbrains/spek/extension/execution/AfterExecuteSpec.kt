package org.jetbrains.spek.extension.execution

import org.jetbrains.spek.extension.GroupExtensionContext

/**
 * @author Ranie Jade Ramiso
 */
interface AfterExecuteSpec {
    fun afterExecuteSpec(spec: GroupExtensionContext)
}
