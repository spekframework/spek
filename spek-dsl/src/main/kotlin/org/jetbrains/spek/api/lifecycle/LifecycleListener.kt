package org.jetbrains.spek.api.lifecycle

import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Experimental
interface LifecycleListener {
    fun beforeExecuteTest(test: TestScope) { }
    fun afterExecuteTest(test: TestScope) { }
    fun beforeExecuteGroup(group: GroupScope) { }
    fun afterExecuteGroup(group: GroupScope) { }
    fun beforeExecuteAction(action: ActionScope) { }
    fun afterExecuteAction(action: ActionScope) { }
}
