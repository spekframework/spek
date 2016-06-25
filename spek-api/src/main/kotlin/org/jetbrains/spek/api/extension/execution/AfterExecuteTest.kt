package org.jetbrains.spek.api.extension.execution

import org.jetbrains.spek.api.extension.Extension
import org.jetbrains.spek.api.extension.TestExtensionContext
import org.jetbrains.spek.api.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface AfterExecuteTest: Extension {
    fun afterExecuteTest(test: TestExtensionContext)
}
