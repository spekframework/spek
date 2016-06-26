package org.jetbrains.spek.engine

import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.extension.ExtensionContext
import org.jetbrains.spek.api.extension.GroupExtensionContext
import org.jetbrains.spek.api.extension.TestExtensionContext
import org.jetbrains.spek.api.extension.execution.AfterExecuteGroup
import org.jetbrains.spek.api.extension.execution.AfterExecuteTest
import org.jetbrains.spek.api.extension.execution.BeforeExecuteGroup
import org.jetbrains.spek.api.extension.execution.BeforeExecuteTest
import org.jetbrains.spek.engine.extension.ExtensionRegistryImpl
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.hierarchical.Node

/**
 * @author Ranie Jade Ramiso
 */
sealed class Scope(uniqueId: UniqueId, val pending: Pending)
    : AbstractTestDescriptor(uniqueId), Node<SpekExecutionContext>, ExtensionContext {
    open class Group(uniqueId: UniqueId, pending: Pending)
        : Scope(uniqueId, pending), GroupExtensionContext {
        override val parent: GroupExtensionContext? by lazy {
            return@lazy if (getParent().isPresent) {
                getParent().get() as GroupExtensionContext
            } else {
                null
            }
        }
        override fun isTest() = false
        override fun isContainer() = true

        override fun before(context: SpekExecutionContext): SpekExecutionContext {
            return super.before(context).apply {
                context.registry.extensions()
                    .filterIsInstance(BeforeExecuteGroup::class.java)
                    .forEach { it.beforeExecuteGroup(this@Group) }
            }
        }

        override fun after(context: SpekExecutionContext) {
            context.registry.extensions()
                .filterIsInstance(AfterExecuteGroup::class.java)
                .forEach { it.afterExecuteGroup(this@Group) }

            super.after(context)
        }
    }

    class Spec(uniqueId: UniqueId, val registry: ExtensionRegistryImpl): Group(uniqueId, Pending.No) {
        override fun prepare(context: SpekExecutionContext): SpekExecutionContext {
            return SpekExecutionContext(registry)
        }
    }

    class Test(uniqueId: UniqueId, pending: Pending, val body: () -> Unit)
        : Scope(uniqueId, pending), TestExtensionContext {
        override val parent: GroupExtensionContext by lazy {
            getParent().get() as GroupExtensionContext
        }

        override fun isTest() = true
        override fun isContainer() = false
        override fun isLeaf() = true

        override fun execute(context: SpekExecutionContext): SpekExecutionContext {
            var throwable: Throwable? = null
            try {
                context.registry.extensions()
                    .filterIsInstance(BeforeExecuteTest::class.java)
                    .forEach { it.beforeExecuteTest(this@Test) }

                context.registry.extensions()
                    .filterIsInstance(FixturesAdapter::class.java)
                    .forEach { it.beforeExecuteTest(this@Test) }

                body.invoke()
            } catch (e: Throwable) {
                throwable = e
            }

            try {

                context.registry.extensions()
                    .filterIsInstance(FixturesAdapter::class.java)
                    .forEach { it.afterExecuteTest(this@Test) }

                context.registry.extensions()
                    .filterIsInstance(AfterExecuteTest::class.java)
                    .forEach { it.afterExecuteTest(this) }

            } catch (e: Throwable) {
                if (throwable == null) {
                    throwable = e
                }
            }

            if (throwable != null) {
                throw throwable
            }
            return context
        }
    }

    override fun getDisplayName() = uniqueId.segments.last().value

    override fun shouldBeSkipped(context: SpekExecutionContext): Node.SkipResult {
        return when(pending) {
            is Pending.Yes -> Node.SkipResult.skip(pending.reason)
            else -> Node.SkipResult.doNotSkip()
        }
    }
}
