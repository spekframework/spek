package org.jetbrains.spek.engine.scope

import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.engine.SpekExecutionContext
import org.jetbrains.spek.engine.memoized.SubjectImpl
import org.junit.gen5.engine.UniqueId
import org.junit.gen5.engine.support.descriptor.AbstractTestDescriptor
import org.junit.gen5.engine.support.hierarchical.Node
import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
sealed class Scope(uniqueId: UniqueId, val pending: Pending)
    : AbstractTestDescriptor(uniqueId), Node<SpekExecutionContext> {
    class Group(uniqueId: UniqueId, pending: Pending)
        : Scope(uniqueId, pending) {
        var subject: SubjectImpl<*>? = null
        val fixtures: Fixtures = Fixtures()
        override fun isTest() = false
        override fun isContainer() = true
        override fun prepare(context: SpekExecutionContext?): SpekExecutionContext? {
            return super.prepare(context).apply {
                if (subject != null) {
                    addListener(subject!!)
                }
            }
        }
    }

    class Test(uniqueId: UniqueId, pending: Pending, val body: TestBody.() -> Unit)
        : Scope(uniqueId, pending), TestBody {
        private var cleanups: LinkedList<() -> Unit> = LinkedList()
        override fun isTest() = true
        override fun isContainer() = false
        override fun isLeaf() = true
        override fun execute(context: SpekExecutionContext): SpekExecutionContext {
            try {
                body.invoke(this)
            }
            catch (e: Throwable) {
                invokeAllCleanups(invokeAllAfterEach(parent.get() as Group, e))
                throw e
            }
            return context
        }

        override fun before(context: SpekExecutionContext): SpekExecutionContext {
            return super.before(context).apply {
                beforeTest(this@Test)
                try {
                    invokeAllBeforeEach(parent.get() as Group)
                } catch (e: Throwable) {
                    invokeAllCleanups(invokeAllAfterEach(parent.get() as Group, e))
                    throw e
                }
            }
        }

        override fun after(context: SpekExecutionContext) {
            val exception = invokeAllCleanups(invokeAllAfterEach(parent.get() as Group))
            if (exception != null)
                throw exception
            context.afterTest(this)
            super.after(context)
        }

        private fun invokeAllBeforeEach(scope: Group) {
            if (scope.parent.isPresent) {
                val parent = scope.parent.get()
                if (!parent.isRoot) {
                    invokeAllBeforeEach(scope.parent.get() as Group)
                }
            }
            scope.fixtures.beforeEach.forEach { it.invoke(this) }
        }

        private fun invokeAllAfterEach(scope: Group, initialException: Throwable? = null): Throwable? {
            val exception = invokeAll(scope.fixtures.afterEach, initialException)
            if (scope.parent.isPresent) {
                val parent = scope.parent.get()
                if (!parent.isRoot) {
                    return invokeAllAfterEach(parent as Group, exception)
                }
            }
            return exception
        }

        private fun invokeAllCleanups(initialException: Throwable? = null) = invokeAll(cleanups, initialException)

        private fun invokeAll(blocks: List<() -> Unit>, initialException: Throwable?): Throwable? {
            var exception = initialException
            for (block in blocks) {
                try {
                    block.invoke()
                } catch (e: Throwable) {
                    if (exception != null) {
// When JDK 7 is officially supported:
//                        exception.addSuppressed(e)
                    } else {
                        exception = e
                    }
                }
            }
            return exception
        }

        override fun onCleanup(block: () -> Unit) {
            cleanups.add(block)
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
