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
                invokeAllCleanups(e)
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
                    invokeAllCleanups(e)
                    throw e
                }
            }
        }

        override fun after(context: SpekExecutionContext) {
            try {
                invokeAllAfterEach(parent.get() as Group)
            } finally {
                invokeAllCleanups()
            }
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

        private fun invokeAllAfterEach(scope: Group) {
            invokeAllCleanups()
            scope.fixtures.afterEach.forEach { it.invoke() }
            if (scope.parent.isPresent) {
                val parent = scope.parent.get()
                if (!parent.isRoot) {
                    invokeAllAfterEach(parent as Group)
                }
            }
        }

        private fun invokeAllCleanups(initialException: Throwable? = null) {
            var exception: Throwable? = null
            for (cleanup in cleanups) {
                try {
                    cleanup.invoke()
                } catch (e: Throwable) {
                    if (initialException != null) {
// When JDK 7 is officially supported:
//                    } else {
//                        initialException.addSuppressed(e)
                    } else if (exception != null) {
// When JDK 7 is officially supported:
//                        exception.addSuppressed(e)
                    } else {
                        exception = e
                    }
                }
            }
            if (exception != null)
                throw exception
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
