package org.jetbrains.spek.extension.execution

import org.jetbrains.spek.extension.Extension
import org.jetbrains.spek.extension.TestExtensionContext
import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface AfterExecuteTest: Extension {
    fun afterExecuteTest(test: TestExtensionContext)
}
