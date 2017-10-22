package org.spekframework.spek2.lifecycle

import org.spekframework.spek2.meta.Experimental

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
