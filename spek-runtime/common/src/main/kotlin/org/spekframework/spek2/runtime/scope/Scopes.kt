package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.*
import org.spekframework.spek2.runtime.execution.ExecutionContext
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueReader
import kotlin.properties.ReadOnlyProperty

sealed class ScopeImpl(
    val id: ScopeId,
    val path: Path,
    val skip: Skip,
    val lifecycleManager: LifecycleManager
) : Scope {

    private val values = mutableMapOf<String, ReadOnlyProperty<Any?, Any?>>()

    abstract fun before(context: ExecutionContext)
    open fun execute(context: ExecutionContext) = Unit
    abstract fun after(context: ExecutionContext)

    fun registerValue(name: String, value: ReadOnlyProperty<Any?, Any?>) {
        values[name] = value
    }

    fun getValue(name: String): ReadOnlyProperty<Any?, Any?> = when {
        values.containsKey(name) -> values[name]!!
        parent != null -> (parent as ScopeImpl).getValue(name)
        else -> throw IllegalArgumentException("No value for '$name'")
    }

    fun removeValue(name: String) = values.remove(name)
}

open class GroupScopeImpl(
    id: ScopeId,
    path: Path,
    override val parent: GroupScope?,
    skip: Skip,
    lifecycleManager: LifecycleManager
) : ScopeImpl(id, path, skip, lifecycleManager), GroupScope {

    private val children = mutableListOf<ScopeImpl>()

    fun addChild(child: ScopeImpl) {
        children.add(child)
    }

    fun getChildren() = children.toList()

    fun filterBy(path: Path) {
        val filteredChildren = children
            .filter { it.path.isRelated(path) }
            .map {
                if (it is GroupScopeImpl) {
                    it.filterBy(path)
                }

                it
            }

        children.clear()
        children.addAll(filteredChildren)
    }

    fun isEmpty() = children.isEmpty()

    override fun before(context: ExecutionContext) {
        lifecycleManager.beforeExecuteGroup(this)
    }

    override fun after(context: ExecutionContext) {
        lifecycleManager.afterExecuteGroup(this)
    }
}

class ActionScopeImpl(
    id: ScopeId,
    path: Path,
    parent: GroupScope?,
    private val body: ActionScopeImpl.(ExecutionContext) -> Unit,
    skip: Skip,
    lifecycleManager: LifecycleManager
) : GroupScopeImpl(id, path, parent, skip, lifecycleManager), ActionScope {

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

class TestScopeImpl(
    id: ScopeId,
    path: Path,
    override val parent: GroupScope,
    private val body: TestBody.() -> Unit,
    skip: Skip,
    lifecycleManager: LifecycleManager
) : ScopeImpl(id, path, skip, lifecycleManager), TestScope {

    override fun before(context: ExecutionContext) {
        lifecycleManager.beforeExecuteTest(this)
    }

    override fun execute(context: ExecutionContext) {
        body.invoke(object : TestBody {
            override fun <T> memoized(): MemoizedValue<T> {
                return MemoizedValueReader(this@TestScopeImpl)
            }
        })
    }

    override fun after(context: ExecutionContext) {
        lifecycleManager.afterExecuteTest(this)
    }
}
