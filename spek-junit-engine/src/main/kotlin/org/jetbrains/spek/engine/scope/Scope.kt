package org.jetbrains.spek.engine.scope

import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.engine.SpekExecutionContext
import org.jetbrains.spek.engine.subject.SubjectImpl
import org.junit.gen5.engine.UniqueId
import org.junit.gen5.engine.support.descriptor.AbstractTestDescriptor
import org.junit.gen5.engine.support.hierarchical.Node

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

    class Test(uniqueId: UniqueId, pending: Pending, val body: () -> Unit)
        : Scope(uniqueId, pending) {
        override fun isTest() = true
        override fun isContainer() = false
        override fun isLeaf() = true
        override fun execute(context: SpekExecutionContext): SpekExecutionContext {
            body.invoke()
            return context
        }

        override fun before(context: SpekExecutionContext): SpekExecutionContext {
            return super.before(context).apply {
                beforeTest(this@Test)
                invokeAllBeforeEach(parent.get() as Group)
            }
        }

        override fun after(context: SpekExecutionContext) {
            invokeAllAfterEach(parent.get() as Group)
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
            scope.fixtures.beforeEach.forEach { it.invoke() }
        }

        private fun invokeAllAfterEach(scope: Group) {
            scope.fixtures.afterEach.forEach { it.invoke() }
            if (scope.parent.isPresent) {
                val parent = scope.parent.get()
                if (!parent.isRoot) {
                    invokeAllAfterEach(parent as Group)
                }
            }
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
