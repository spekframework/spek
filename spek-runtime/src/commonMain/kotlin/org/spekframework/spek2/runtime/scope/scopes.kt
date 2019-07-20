package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.*
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueAdapter2
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueReader
import kotlin.properties.ReadOnlyProperty

sealed class ScopeImpl(
    val id: ScopeId,
    val path: Path,
    val skip: Skip,
    val lifecycleManager: LifecycleManager
) : Scope {

    private val values = mutableMapOf<String, MemoizedValueAdapter2<*>>()

    abstract fun before()
    abstract fun execute()
    abstract fun after()

    protected fun registerValue(name: String, value: MemoizedValueAdapter2<*>) {
        values[name] = value
    }

    fun getValue(name: String): ReadOnlyProperty<Any?, Any?> = when {
        values.containsKey(name) -> values[name]!!
        parent != null -> (parent as ScopeImpl).getValue(name)
        else -> throw IllegalArgumentException("No value for '$name'")
    }
}

open class GroupScopeImpl(
    id: ScopeId,
    path: Path,
    override val parent: GroupScope?,
    skip: Skip,
    lifecycleManager: LifecycleManager,
    preserveExecutionOrder: Boolean,
    val failFast: Boolean = false
) : ScopeImpl(id, path, skip, lifecycleManager), GroupScope {

    private val children = mutableListOf<ScopeImpl>()
    private val declarations = mutableListOf<ScopeDeclaration>()

    fun addChild(child: ScopeImpl) {
        children.add(child)
    }

    fun removeChild(child: ScopeImpl) {
        children.remove(child)
    }

    fun getChildren() = children.toList()

    fun filterBy(path: Path) {
        val filteredChildren = children
            .filter { it.path.intersects(path) }
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

    override fun before() = lifecycleManager.beforeExecuteGroup(this)
    override fun execute() = Unit
    override fun after() = lifecycleManager.afterExecuteGroup(this)

    fun registerDeclaration(declaration: ScopeDeclaration) {
        declarations.add(declaration)

        if (declaration is ScopeDeclaration.Memoized<*>) {
            registerValue(declaration.name, declaration.adapter)
        }
    }

    fun getDeclarations(): List<ScopeDeclaration> = declarations.toList()
}

class TestScopeImpl(
    id: ScopeId,
    path: Path,
    override val parent: GroupScope,
    val timeout: Long,
    val body: TestBody.() -> Unit,
    skip: Skip,
    lifecycleManager: LifecycleManager
) : ScopeImpl(id, path, skip, lifecycleManager), TestScope {

    override fun before() = lifecycleManager.beforeExecuteTest(this)

    override fun execute() {
        body.invoke(object : TestBody {
            override fun <T> memoized(): MemoizedValue<T> {
                return MemoizedValueReader(this@TestScopeImpl)
            }
        })
    }

    override fun after() = lifecycleManager.afterExecuteTest(this)
}

