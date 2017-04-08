package org.jetbrains.spek.engine

import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.lifecycle.ActionScope
import org.jetbrains.spek.api.lifecycle.GroupScope
import org.jetbrains.spek.api.lifecycle.TestScope
import org.jetbrains.spek.engine.lifecycle.LifecycleManager
import org.junit.platform.engine.TestDescriptor.Type.CONTAINER
import org.junit.platform.engine.TestDescriptor.Type.TEST
import org.junit.platform.engine.TestSource
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.hierarchical.Node

/**
 * @author Ranie Jade Ramiso
 */
sealed class Scope(uniqueId: UniqueId, val pending: Pending, val source: TestSource?,
                   val lifecycleManager: LifecycleManager)
    : AbstractTestDescriptor(uniqueId, uniqueId.segments.last().value), Node<SpekExecutionContext>,
      org.jetbrains.spek.api.lifecycle.Scope {

    init {
        if (source != null) {
            setSource(source)
        }
    }

    override val parent: GroupScope? by lazy {
        return@lazy if (getParent().isPresent) {
            getParent().get() as GroupScope
        } else {
            null
        }
    }

    class Action(uniqueId: UniqueId, pending: Pending,
                 source: TestSource?,
                 lifecycleManager: LifecycleManager,
                 private val body: Action.(SpekExecutionContext) -> Unit)
        : Scope(uniqueId, pending, source, lifecycleManager), ActionScope {
        override fun getType() = CONTAINER
        override fun hasTests() = true

        override fun before(context: SpekExecutionContext): SpekExecutionContext {
            lifecycleManager.beforeExecuteAction(this)
            return context
        }

        override fun execute(context: SpekExecutionContext): SpekExecutionContext {
            val collector = ThrowableCollector()

            if (collector.isEmpty()) {
                collector.executeSafely { body.invoke(this, context) }
            }

            collector.assertEmpty()
            return context
        }

        override fun after(context: SpekExecutionContext) {
            lifecycleManager.afterExecuteAction(this)
        }
    }

    open class Group(uniqueId: UniqueId, pending: Pending,
                     source: TestSource?,
                     lifecycleManager: LifecycleManager)
        : Scope(uniqueId, pending, source, lifecycleManager), GroupScope {
        override fun getType() = CONTAINER

        override fun before(context: SpekExecutionContext): SpekExecutionContext {
            lifecycleManager.beforeExecuteGroup(this@Group)
            return context
        }

        override fun after(context: SpekExecutionContext) {
            lifecycleManager.afterExecuteGroup(this@Group)
        }
    }

    class Test(uniqueId: UniqueId, pending: Pending, source: TestSource?, lifecycleManager: LifecycleManager, val body: TestBody.() -> Unit)
        : Scope(uniqueId, pending, source, lifecycleManager), TestScope {
        override val parent: GroupScope by lazy {
            getParent().get() as GroupScope
        }

        override fun getType() = TEST
        override fun isLeaf() = true

        override fun before(context: SpekExecutionContext): SpekExecutionContext {
            lifecycleManager.beforeExecuteTest(this)
            return context
        }

        override fun after(context: SpekExecutionContext) {
            lifecycleManager.afterExecuteTest(this)
        }

        override fun execute(context: SpekExecutionContext): SpekExecutionContext {
            val collector = ThrowableCollector()
            if (collector.isEmpty()) {

                if (collector.isEmpty()) {
                    collector.executeSafely { body.invoke(object: TestBody {}) }
                }
            }

            collector.assertEmpty()

            return context
        }
    }

    override fun shouldBeSkipped(context: SpekExecutionContext): Node.SkipResult {
        return when(pending) {
            is Pending.Yes -> Node.SkipResult.skip(pending.reason)
            else -> Node.SkipResult.doNotSkip()
        }
    }
}
