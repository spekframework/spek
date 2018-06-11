package org.spekframework.spek2.runtime

import org.spekframework.spek2.dsl.*
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.runtime.execution.ExecutionContext
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueCreator
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueReader
import org.spekframework.spek2.runtime.scope.*

open class Collector(
        val root: GroupScopeImpl,
        private val lifecycleManager: LifecycleManager,
        private val fixtures: FixturesAdapter
) : Root {

    private val ids = mutableMapOf<String, Int>()

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

    override fun group(description: String, pending: Pending, body: GroupBody.() -> Unit) {
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

    private fun idFor(description: String): ScopeId {
        val current = ids.getOrPut(description) { 0 } + 1
        ids[description] = current

        return ScopeId(ScopeType.Scope, if (current > 1) "$description [$current]" else description)
    }
}

class ActionCollector(
        val root: ActionScopeImpl,
        private val lifecycleManager: LifecycleManager,
        private val context: ExecutionContext,
        private val idFor: (String) -> ScopeId
) : ActionBody {

    override fun <T> memoized(): MemoizedValue<T> {
        return MemoizedValueReader(root)
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
        context.executionListener.apply {
            testExecutionStart(test)
            try {
                test.execute(context)
                testExecutionFinish(test, ExecutionResult.Success)
            } catch (e: Throwable) {
                testExecutionFinish(test, ExecutionResult.Failure(e))
            }
        }
    }

}
