package org.spekframework.spek2.lifecycle

import org.spekframework.spek2.meta.Experimental

@Experimental
interface LifecycleListener {
    fun beforeExecuteTest(test: TestScope) {}
    fun afterExecuteTest(test: TestScope) {}
    fun beforeExecuteGroup(group: GroupScope) {}
    fun afterExecuteGroup(group: GroupScope) {}
    fun beforeExecuteAction(action: ActionScope) {}
    fun afterExecuteAction(action: ActionScope) {}
}
