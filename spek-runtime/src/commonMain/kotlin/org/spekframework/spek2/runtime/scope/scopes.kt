package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.dsl.Fixture
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.lifecycle.Scope
import org.spekframework.spek2.lifecycle.TestScope
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

    abstract fun before()
    abstract fun after()

    fun registerValue(name: String, value: ReadOnlyProperty<Any?, Any?>) {
        values[name] = value
    }

    fun getValue(name: String): ReadOnlyProperty<Any?, Any?> = when {
        values.containsKey(name) -> values[name]!!
        parent != null -> (parent as ScopeImpl).getValue(name)
        else -> throw IllegalArgumentException("No value for '$name'")
    }
}

class GroupScopeImpl(
    id: ScopeId,
    path: Path,
    override val parent: GroupScope?,
    skip: Skip,
    lifecycleManager: LifecycleManager,
    preserveExecutionOrder: Boolean,
    val failFast: Boolean = false
) : ScopeImpl(id, path, skip, lifecycleManager), GroupScope {
    private val fixtures = Fixtures()
    private val children = mutableListOf<ScopeImpl>()

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
    override fun after() = lifecycleManager.afterExecuteGroup(this)

    fun beforeEachTest(fixture: Fixture) {
        fixtures.beforeEachTest(fixture)
    }

    fun afterEachTest(fixture: Fixture) {
        fixtures.afterEachTest(fixture)
    }

    fun beforeGroup(fixture: Fixture) {
        fixtures.beforeGroup(fixture)
    }

    fun afterGroup(fixture: Fixture) {
        fixtures.afterGroup(fixture)
    }

    fun invokeBeforeTestFixtures() {
        (parent as? GroupScopeImpl)?.invokeBeforeTestFixtures()
        fixtures.invokeBeforeTestFixtures()
    }

    fun invokeAfterTestFixtures() {
        fixtures.invokeAfterTestFixtures()
        (parent as? GroupScopeImpl)?.invokeAfterTestFixtures()
    }

    fun invokeBeforeGroupFixtures(inheritableOnly: Boolean) {
        // we only want to execute fixtures that we inherit aka beforeEachGroup
        (parent as? GroupScopeImpl)?.invokeBeforeGroupFixtures(true)
        fixtures.invokeBeforeGroupFixtures(inheritableOnly)
    }

    fun invokeAfterGroupFixtures(inheritableOnly: Boolean) {
        // we only want to execute fixtures that we inherit aka afterEachGroup
        fixtures.invokeAfterGroupFixtures(inheritableOnly)
        (parent as? GroupScopeImpl)?.invokeAfterGroupFixtures(true)
    }
}

class TestScopeImpl(
    id: ScopeId,
    path: Path,
    override val parent: GroupScope,
    val timeout: Long,
    private val body: TestBody.() -> Unit,
    skip: Skip,
    lifecycleManager: LifecycleManager
) : ScopeImpl(id, path, skip, lifecycleManager), TestScope {

    override fun before() = lifecycleManager.beforeExecuteTest(this)

    fun execute() {
        body.invoke(object : TestBody {
            override fun <T> memoized(): MemoizedValue<T> {
                return MemoizedValueReader(this@TestScopeImpl)
            }
        })
    }

    override fun after() = lifecycleManager.afterExecuteTest(this)

    fun invokeBeforeTestFixtures() {
        (parent as? GroupScopeImpl)?.invokeBeforeTestFixtures()
    }

    fun invokeAfterTestFixtures() {
        (parent as? GroupScopeImpl)?.invokeAfterTestFixtures()
    }
}
