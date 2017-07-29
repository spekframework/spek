package org.spekframework.runtime

import org.jetbrains.spek.api.dsl.ActionBody
import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.api.lifecycle.LifecycleListener
import org.spekframework.runtime.execution.ExecutionContext
import org.spekframework.runtime.lifecycle.LifecycleAwareAdapter
import org.spekframework.runtime.lifecycle.LifecycleManager
import org.spekframework.runtime.scope.ActionScopeImpl
import org.spekframework.runtime.scope.GroupScopeImpl
import org.spekframework.runtime.scope.ScopeId
import org.spekframework.runtime.scope.TestScopeImpl

open class Collector(val root: GroupScopeImpl,
                     val lifecycleManager: LifecycleManager,
                     val fixtures: FixturesAdapter): Spec {
    val ids = mutableMapOf<String, Int>()

    override fun <T> memoized(mode: CachingMode, factory: () -> T): LifecycleAware<T> {
        val adapter = when (mode) {
            CachingMode.GROUP -> LifecycleAwareAdapter.GroupCachingModeAdapter(factory)
            CachingMode.TEST -> LifecycleAwareAdapter.TestCachingModeAdapter(factory)
            CachingMode.SCOPE -> LifecycleAwareAdapter.ScopeCachingModeAdapter(root, factory)
        }
        return adapter.apply {
            registerListener(this)
        }
    }

    override fun registerListener(listener: LifecycleListener) {
        lifecycleManager.addListener(listener)
    }

    override fun group(description: String, pending: Pending, body: SpecBody.() -> Unit) {
        val group = GroupScopeImpl(
            idFor(description),
            root.path.resolve(description),
            root,
            pending,
            lifecycleManager
        )
        root.addChild(group)
        val collector = Collector(group, lifecycleManager, fixtures)
        try {
            body.invoke(collector)
        } catch (e: Throwable) {
            collector.beforeGroup { throw e }
            group.addChild(TestScopeImpl(
                idFor("Group Failure"),
                root.path.resolve("Group Failure"),
                root,
                {},
                pending,
                lifecycleManager
            ))
        }

    }

    override fun action(description: String, pending: Pending, body: ActionBody.() -> Unit) {
        val action = ActionScopeImpl(
            idFor(description),
            root.path.resolve(description),
            root,
            {
                body.invoke(ActionCollector(this, lifecycleManager, it, this@Collector::idFor))
            },
            pending,
            lifecycleManager
        )

        root.addChild(action)
    }

    override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
        val test = TestScopeImpl(
            idFor(description),
            root.path.resolve(description),
            root,
            body,
            pending,
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

    protected fun idFor(description: String): ScopeId {
        val current = ids.computeIfAbsent(description) { 0 } + 1
        ids.put(description, current)

        return if (current > 1) {
            ScopeId("scope", "$description [$current]")
        } else {
            ScopeId("scope", description)
        }
    }
}

class ActionCollector(val root: ActionScopeImpl, val lifecycleManager: LifecycleManager, val context: ExecutionContext, val idFor: (String) -> ScopeId): ActionBody {

    override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
        val test = TestScopeImpl(
            idFor(description),
            root.path.resolve(description),
            root,
            body,
            pending,
            lifecycleManager
        )
        root.addChild(test)
        context.runtimeExecutionListener.dynamicTestRegistered(test, context)
    }

}
