package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.lifecycle.ExecutionResult
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LetValue
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.runtime.Collector
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.Path
import kotlin.native.concurrent.ThreadLocal
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LetValueCreator<T>(
    val factory: () -> T, val root: Root, val afterGroupDeclaration: (() -> Unit) -> Unit
) : LetValue.PropertyCreator<T> {
    override fun provideDelegate(thisRef: Any?, property: KProperty<*>): ReadOnlyProperty<Any?, T> {
        return LetValueGetter(factory, root, property.name).also {
            root.registerListener(it)
            root.beforeEachTest { LetValuesState.inTest = true }

            // This causes the afterEachTest to run last in the declaring group.
            afterGroupDeclaration {
                root.afterEachTest { it.reset() }
            }
        }
    }
}

class LetValueGetter<T>(baseFactory: () -> T, val root: Root, val name: String) : ReadOnlyProperty<Any?, T>, LifecycleListener {
    private val paths = hashMapOf((root as Collector).root.path to baseFactory)
    private var currentPath: Path? = null
    private val stack = mutableListOf<Path?>()

    private var initializedForTest = false
    private var valueForTest: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (!LetValuesState.inTest) {
            if (LetValuesState.current != null) {
                throw IllegalStateException("$name can't be used from beforeEachGroup or afterEachGroup")
            }
            LetValuesState.current = this
            @Suppress("UNCHECKED_CAST")
            return null as T
        }

        if (initializedForTest) {
            return valueForTest!!
        }

        var searchPath = currentPath
        while (searchPath != null && !paths.containsKey(searchPath)) {
            searchPath = searchPath.parent
        }
        val fn = paths[searchPath] ?: throw IllegalStateException("no let value for $currentPath")
        return fn().also {
            valueForTest = it
            initializedForTest = true
        }
    }

    fun override(path: Path, factory: () -> T) {
        if (paths.containsKey(path)) {
            throw IllegalStateException("value already given for $name in $path")
        }
        paths[path] = factory
    }

    fun reset() {
        initializedForTest = false
        valueForTest = null
        LetValuesState.inTest = false
    }

    override fun beforeExecuteGroup(group: GroupScope) {
        stack.add(currentPath)
        currentPath = (group as GroupScopeImpl).path
    }

    override fun afterExecuteGroup(group: GroupScope, result: ExecutionResult) {
        if (LetValuesState.current != null) {
            throw IllegalStateException("$name can't be used from beforeEachGroup or afterEachGroup")
        }
        currentPath = stack.removeAt(stack.size - 1)
    }

    companion object {
        internal fun <T> override(path: Path, factory: () -> T) {
            LetValuesState.override(path, factory)
        }
    }
}

@ThreadLocal
private object LetValuesState {
    internal var current: LetValueGetter<*>? = null

    internal var inTest: Boolean = false

    fun <T> override(path: Path, factory: () -> T) {
        val contextLetValue = current
        current = null

        if (contextLetValue == null) {
            throw IllegalStateException("no context for value override")
        } else {
            @Suppress("UNCHECKED_CAST")
            contextLetValue.override(path, factory as () -> Nothing)
        }
    }
}