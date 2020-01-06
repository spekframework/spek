package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.lifecycle.*
import org.spekframework.spek2.runtime.Collector
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.Path
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LetValueCreator<T>(
        val factory: () -> T, val root: Root, val afterGroupDeclaration: (() -> Unit) -> Unit
) : LetValue.PropertyCreator<T> {
    override fun provideDelegate(thisRef: Any?, property: KProperty<*>): ReadOnlyProperty<Any?, LetValue<T>> {
        return LetValueHolder(factory, root, property.name).also {
            root.registerListener(it)
            root.beforeEachTest { it.inTest = true }

            // This causes the afterEachTest to run last in the declaring group.
            afterGroupDeclaration {
                root.afterEachTest { it.reset() }
            }
        }
    }
}

class LetValueHolder<T>(baseFactory: () -> T, val root: Root, val name: String) : LetValue<T>, LifecycleListener {
    val paths = hashMapOf((root as Collector).root.path to baseFactory)
    private var currentPath: Path? = null
    private val stack = mutableListOf<Path?>()

    var inTest: Boolean = false
    private var initializedForTest = false
    private var valueForTest: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): LetValue<T> = this

    override operator fun invoke(): T {
        if (!inTest) throw IllegalStateException("$name() can't be used from beforeEachGroup or afterEachGroup")

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
        inTest = false
    }

    override fun beforeExecuteGroup(group: GroupScope) {
        stack.add(currentPath)
        currentPath = (group as GroupScopeImpl).path
    }

    override fun afterExecuteGroup(group: GroupScope, result: ExecutionResult) {
        currentPath = stack.removeAt(stack.size - 1)
    }
}
