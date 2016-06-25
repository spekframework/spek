package org.jetbrains.spek.engine

import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.extension.ExtensionContext
import org.jetbrains.spek.api.extension.GroupExtensionContext
import org.jetbrains.spek.api.extension.TestExtensionContext
import org.jetbrains.spek.api.extension.execution.AfterExecuteTest
import org.jetbrains.spek.api.extension.execution.BeforeExecuteTest
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.hierarchical.Node

/**
 * @author Ranie Jade Ramiso
 */
sealed class Scope(uniqueId: UniqueId, val pending: Pending)
    : AbstractTestDescriptor(uniqueId), Node<SpekExecutionContext>, ExtensionContext {
    class Group(uniqueId: UniqueId, pending: Pending)
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
            body.invoke()
            return context
        }

        override fun before(context: SpekExecutionContext): SpekExecutionContext {
            return super.before(context).apply {
                context.extensions()
                    .filterIsInstance(BeforeExecuteTest::class.java)
                    .forEach { it.beforeExecuteTest(this@Test) }

            }
        }

        override fun after(context: SpekExecutionContext) {
            context.extensions()
                .filterIsInstance(AfterExecuteTest::class.java)
                .forEach { it.afterExecuteTest(this) }
            super.after(context)
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
