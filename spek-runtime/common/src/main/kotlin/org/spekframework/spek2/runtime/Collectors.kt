package org.spekframework.spek2.runtime

import org.spekframework.spek2.dsl.*
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueCreator
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueReader
import org.spekframework.spek2.runtime.scope.*

open class Collector(
    val root: GroupScopeImpl,
    private val lifecycleManager: LifecycleManager,
    private val fixtures: FixturesAdapter,
    order: Order
) : Root {

    private val ids: MutableMap<String, Int> = when (order) {
        Order.Unspecified -> HashMap()
        Order.Strict -> LinkedHashMap()
    }

    override fun <T> memoized(mode: CachingMode, factory: () -> T): MemoizedValue<T> = memoized(mode, factory) { }

    override fun <T> memoized(mode: CachingMode, factory: () -> T, destructor: (T) -> Unit): MemoizedValue<T> {
        return MemoizedValueCreator(
            root,
            mode,
            factory,
            destructor
        )
    }

    override fun <T> memoized(): MemoizedValue<T> {
        return MemoizedValueReader(root)
    }

    override fun registerListener(listener: LifecycleListener) {
        lifecycleManager.addListener(listener)
    }

    override fun group(description: String, skip: Skip, order: Order, body: GroupBody.() -> Unit) {
        val group = GroupScopeImpl(
            idFor(description),
            root.path.resolve(description),
            root,
            skip,
            lifecycleManager
        )
        root.addChild(group)
        val collector = Collector(group, lifecycleManager, fixtures, order)
        try {
            body.invoke(collector)
        } catch (e: Throwable) {
            collector.beforeGroup { throw e }
            group.addChild(
                TestScopeImpl(
                    idFor("Group Failure"),
                    root.path.resolve("Group Failure"),
                    root,
                    {},
                    skip,
                    lifecycleManager
                )
            )
        }

    }

    override fun test(description: String, skip: Skip, body: TestBody.() -> Unit) {
        val test = TestScopeImpl(
            idFor(description),
            root.path.resolve(description),
            root,
            body,
            skip,
            lifecycleManager
        )
        root.addChild(test)
    }

    override fun beforeEachTest(callback: () -> Unit) {
        fixtures.registerBeforeEachTest(root, callback)
    }

    override fun afterEachTest(callback: () -> Unit) {
        fixtures.registerAfterEachTest(root, callback)
    }

    override fun beforeGroup(callback: () -> Unit) {
        fixtures.registerBeforeGroup(root, callback)
    }

    override fun afterGroup(callback: () -> Unit) {
        fixtures.registerAfterGroup(root, callback)
    }

    private fun idFor(description: String): ScopeId {
        val current = ids.getOrPut(description) { 0 } + 1
        ids[description] = current

        return ScopeId(ScopeType.Scope, if (current > 1) "$description [$current]" else description)
    }
}
