package org.jetbrains.spek.engine.lifecycle

import org.jetbrains.spek.api.lifecycle.ActionScope
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.GroupScope
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.api.lifecycle.LifecycleListener
import org.jetbrains.spek.api.lifecycle.TestScope
import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 */
class LifecycleAwareAdapter<T>(val mode: CachingMode, val factory: () -> T): LifecycleAware<T>, LifecycleListener {
    var cached: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>) = invoke()

    override fun invoke(): T {
        if (cached == null) {
            cached = factory()
        }
        return cached!!
    }

    override fun afterExecuteTest(test: TestScope) {
        if (test.parent !is ActionScope) {
            if (mode == CachingMode.TEST) {
                cached = null
            }
        }
    }

    override fun afterExecuteGroup(group: GroupScope) {
        if (mode == CachingMode.GROUP) {
            cached = null
        }
    }

    override fun afterExecuteAction(action: ActionScope) {
        cached = null
    }
}
