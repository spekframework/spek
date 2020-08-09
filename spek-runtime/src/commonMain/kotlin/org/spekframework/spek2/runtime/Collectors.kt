package org.spekframework.spek2.runtime

import org.spekframework.spek2.dsl.*
import org.spekframework.spek2.lifecycle.*
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueCreator
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueReader
import org.spekframework.spek2.runtime.scope.*

class Collector(
    val root: GroupScopeImpl,
    private val lifecycleManager: LifecycleManager,
    override val defaultCachingMode: CachingMode,
    override var defaultTimeout: Long
) : Root {

    private val finalizers = mutableListOf<() -> Unit>()
    private val ids = linkedMapOf<String, Int>()

    fun finalize() {
        finalizers.forEach { it.invoke() }
        finalizers.clear()
    }

    override fun <T> memoized(mode: CachingMode, factory: suspend () -> T): MemoizedValue<T> = memoized(mode, factory) { }

    override fun <T> memoized(mode: CachingMode, factory: suspend () -> T, destructor: suspend (T) -> Unit): MemoizedValue<T> {
        // scope values automatically register an afterXXX fixture
        // when de-initializing - this means that any afterXXX fixture
        // declared after the memoized can't access it.
        // this custom lifecycle aware object delays the registration
        // of the afterXXX fixtures called by MemoizedValueAdapter.
        // The fixtures are only registered when Collector.finalize() is invoked,
        // which happens after a group have been discovered.
        val lifecycleAware = object: LifecycleAware by this {
            override fun afterEachTest(fixture: Fixture) {
                with(this@Collector) {
                    finalizers.add { afterEachTest(fixture) }
                }
            }

            override fun afterEachGroup(fixture: Fixture) {
                with(this@Collector) {
                    finalizers.add { afterEachGroup(fixture) }
                }
            }

            override fun afterGroup(fixture: Fixture) {
                with(this@Collector) {
                    finalizers.add { afterGroup(fixture) }
                }
            }
        }

        return MemoizedValueCreator(
            root,
            mode,
            lifecycleAware,
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

    override fun group(description: String, skip: Skip, defaultCachingMode: CachingMode, preserveExecutionOrder: Boolean, failFast: Boolean, body: GroupBody.() -> Unit) {
        // TODO: combine failFast and preserveExecutionOrder into a single parameter.
        require(failFast == preserveExecutionOrder) { "failFast and preserveExecutionOrder must have the same value!" }
        if (root.failFast) {
            throw AssertionError("Fail fast groups can only contain test scopes!")
        }

        val group = GroupScopeImpl(
            idFor(description),
            root.path.resolve(description),
            root,
            skip,
            lifecycleManager,
            preserveExecutionOrder,
            failFast
        )
        root.addChild(group)
        val cachingMode = if (defaultCachingMode == CachingMode.INHERIT) {
            this.defaultCachingMode
        } else {
            defaultCachingMode
        }
        val collector = Collector(group, lifecycleManager, cachingMode, defaultTimeout)
        try {
            require(description.isNotEmpty()) { "Empty description for group." }
            body.invoke(collector)
            collector.finalize()
        } catch (e: Throwable) {
            collector.beforeGroup { throw e }
            group.addChild(
                TestScopeImpl(
                    idFor("Group Failure"),
                    root.path.resolve("Group Failure"),
                    root,
                    defaultTimeout,
                    {},
                    skip,
                    lifecycleManager
                )
            )
        }
    }

    override fun test(description: String, skip: Skip, timeout: Long, body: suspend TestBody.() -> Unit) {
        require(description.isNotEmpty()) { "Empty description for test." }
        val test = TestScopeImpl(
            idFor(description),
            root.path.resolve(description),
            root,
            timeout,
            body,
            skip,
            lifecycleManager
        )
        root.addChild(test)
    }

    override fun beforeEachTest(fixture: Fixture) {
        root.beforeEachTest(fixture)
    }

    override fun afterEachTest(fixture: Fixture) {
        root.afterEachTest(fixture)
    }

    override fun beforeEachGroup(fixture: Fixture) {
        root.beforeEachGroup(fixture)
    }

    override fun afterEachGroup(fixture: Fixture) {
        root.afterEachGroup(fixture)
    }

    override fun beforeGroup(fixture: Fixture) {
        root.beforeGroup(fixture)
    }

    override fun afterGroup(fixture: Fixture) {
        root.afterGroup(fixture)
    }

    private fun idFor(description: String): ScopeId {
        val current = ids.getOrPut(description) { 0 } + 1
        ids[description] = current

        return ScopeId(ScopeType.Scope, if (current > 1) "$description [$current]" else description)
    }
}
