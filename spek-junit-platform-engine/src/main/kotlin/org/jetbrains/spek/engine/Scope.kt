package org.jetbrains.spek.engine

import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.engine.extension.ExtensionRegistryImpl
import org.jetbrains.spek.extension.ExtensionContext
import org.jetbrains.spek.extension.GroupExtensionContext
import org.jetbrains.spek.extension.TestExtensionContext
import org.jetbrains.spek.extension.execution.*
import org.junit.platform.engine.TestSource
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.hierarchical.Node

/**
 * @author Ranie Jade Ramiso
 */
sealed class Scope(uniqueId: UniqueId, val pending: Pending, val source: TestSource?)
    : AbstractTestDescriptor(uniqueId, uniqueId.segments.last().value), Node<SpekExecutionContext>, ExtensionContext {

    init {
        if (source != null) {
            setSource(source)
        }
    }

    open class Group(uniqueId: UniqueId, pending: Pending,
                     source: TestSource?,
                     override val lazy: Boolean,
                     val body: Group.(SpekExecutionContext) -> Unit)
        : Scope(uniqueId, pending, source), GroupExtensionContext {
        override val parent: GroupExtensionContext? by lazy {
            return@lazy if (getParent().isPresent) {
                getParent().get() as GroupExtensionContext
            } else {
                null
            }
        }

        override fun isTest() = false
        override fun isContainer() = true

        override fun hasTests(): Boolean {
            return if (lazy) {
                true
            } else {
                super.hasTests()
            }
        }

        override fun before(context: SpekExecutionContext): SpekExecutionContext {
            return super.before(context).apply {
                context.registry.extensions()
                    .filterIsInstance(BeforeExecuteGroup::class.java)
                    .forEach { it.beforeExecuteGroup(this@Group) }
            }
        }

        override fun execute(context: SpekExecutionContext): SpekExecutionContext {
            val collector = ThrowableCollector()

            if (lazy) {
                context.registry.extensions()
                    .filterIsInstance(FixturesAdapter::class.java)
                    .forEach {
                        collector.executeSafely { it.beforeExecuteGroup(this) }
                    }
            }

            if (collector.isEmpty()) {
                collector.executeSafely { body.invoke(this, context) }
            }

            if (lazy) {
                context.registry.extensions()
                    .filterIsInstance(FixturesAdapter::class.java)
                    .forEach {
                        collector.executeSafely { it.afterExecuteGroup(this) }
                    }
            }

            collector.assertEmpty()
            return context
        }

        override fun after(context: SpekExecutionContext) {
            context.registry.extensions()
                .filterIsInstance(AfterExecuteGroup::class.java)
                .forEach { it.afterExecuteGroup(this@Group) }

            super.after(context)
        }
    }

    class Spec(uniqueId: UniqueId, source: TestSource?, val registry: ExtensionRegistryImpl, val nested: Boolean)
        : Group(uniqueId, Pending.No, source, false, {}) {
        override fun prepare(context: SpekExecutionContext): SpekExecutionContext {
            return SpekExecutionContext(registry, context.executionRequest)
        }

        override fun before(context: SpekExecutionContext): SpekExecutionContext {
            if (!nested) {
                context.registry.extensions()
                    .filterIsInstance(BeforeExecuteSpec::class.java)
                    .forEach { it.beforeExecuteSpec(this@Spec) }
            } else {
                return super.before(context)
            }
            return context
        }

        override fun after(context: SpekExecutionContext) {
            if (!nested) {
                context.registry.extensions()
                    .filterIsInstance(AfterExecuteSpec::class.java)
                    .forEach { it.afterExecuteSpec(this@Spec) }
            } else {
                super.after(context)
            }
        }
    }

    class Test(uniqueId: UniqueId, pending: Pending, source: TestSource?, val body: () -> Unit)
        : Scope(uniqueId, pending, source), TestExtensionContext {
        override val parent: GroupExtensionContext by lazy {
            getParent().get() as GroupExtensionContext
        }

        override fun isTest() = true
        override fun isContainer() = false
        override fun isLeaf() = true

        override fun execute(context: SpekExecutionContext): SpekExecutionContext {
            val collector = ThrowableCollector()

            context.registry.extensions()
                .filterIsInstance(BeforeExecuteTest::class.java)
                .forEach {
                    collector.executeSafely { it.beforeExecuteTest(this@Test) }
                }

            if (collector.isEmpty()) {
                if (!parent.lazy) {
                    context.registry.extensions()
                        .filterIsInstance(FixturesAdapter::class.java)
                        .forEach {
                            collector.executeSafely { it.beforeExecuteTest(this@Test) }
                        }
                }

                if (collector.isEmpty()) {
                    collector.executeSafely { body.invoke() }
                }
            }


            if (!parent.lazy) {
                context.registry.extensions()
                    .filterIsInstance(FixturesAdapter::class.java)
                    .forEach {
                        collector.executeSafely { it.afterExecuteTest(this@Test) }
                    }
            }

            context.registry.extensions()
                .filterIsInstance(AfterExecuteTest::class.java)
                .forEach {
                    collector.executeSafely { it.afterExecuteTest(this) }
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
