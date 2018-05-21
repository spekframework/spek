package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.dsl.Pending
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.ActionScope
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.Scope
import org.spekframework.spek2.lifecycle.TestScope
import org.spekframework.spek2.runtime.execution.ExecutionContext
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import kotlin.properties.ReadOnlyProperty

sealed class ScopeImpl(val id: ScopeId,
                       val path: Path,
                       val pending: Pending,
                       val lifecycleManager: LifecycleManager): Scope {
    private val values = mutableMapOf<String, ReadOnlyProperty<Any?, Any?>>()
    abstract fun before(context: ExecutionContext)
    open fun execute(context: ExecutionContext) { }
    abstract fun after(context: ExecutionContext)

    fun registerValue(name: String, value: ReadOnlyProperty<Any?, Any?>) {
        values[name] = value
    }

    fun getValue(name: String): ReadOnlyProperty<Any?, Any?> {
        return when {
            values.containsKey(name) -> values[name]!!
            parent != null -> (parent as ScopeImpl).getValue(name)
            else -> throw IllegalArgumentException("No value for '$name'")
        }
    }


    fun removeValue(name: String) = values.remove(name)
}

open class GroupScopeImpl(id: ScopeId,
                          path: Path,
                          override val parent: GroupScope?,
                          pending: Pending,
                          lifecycleManager: LifecycleManager)
    : ScopeImpl(id, path, pending, lifecycleManager), GroupScope {
    private val children = mutableListOf<ScopeImpl>()

    fun addChild(child: ScopeImpl) {
        children.add(child)
    }

    fun getChildren() = children.toList()

    fun filterBy(path: Path) {
        val tmp = mutableListOf<ScopeImpl>()
        children.filter { it.path.isRelated(path) }
            .forEach {
                if (it is GroupScopeImpl) {
                    it.filterBy(path)
                }
                tmp.add(it)
            }

        children.clear()
        children.addAll(tmp)
    }

    fun isEmpty() = children.isEmpty()

    override fun before(context: ExecutionContext) {
        lifecycleManager.beforeExecuteGroup(this)
    }
    override fun after(context: ExecutionContext) {
        lifecycleManager.afterExecuteGroup(this)
    }
}
class ActionScopeImpl(id: ScopeId,
                      path: Path,
                      parent: GroupScope?,
                      val body: ActionScopeImpl.(ExecutionContext) -> Unit,
                      pending: Pending,
                      lifecycleManager: LifecycleManager)
    : GroupScopeImpl(id, path, parent, pending, lifecycleManager), ActionScope {
    override fun before(context: ExecutionContext) {
        lifecycleManager.beforeExecuteAction(this)
    }
    override fun execute(context: ExecutionContext) {
        body.invoke(this, context)
    }
    override fun after(context: ExecutionContext) {
        lifecycleManager.afterExecuteAction(this)
    }
}

class TestScopeImpl(id: ScopeId,
                    path: Path,
                    override val parent: GroupScope,
                    val body: TestBody.() -> Unit,
                    pending: Pending,
                    lifecycleManager: LifecycleManager)
    : ScopeImpl(id, path, pending, lifecycleManager), TestScope {
    override fun before(context: ExecutionContext) {
        lifecycleManager.beforeExecuteTest(this)
    }
    override fun execute(context: ExecutionContext) {
        body.invoke(object: TestBody {})
    }
    override fun after(context: ExecutionContext) {
        lifecycleManager.afterExecuteTest(this)
    }
}
