package org.spekframework.spek2.junit

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestSource
import org.junit.platform.engine.TestTag
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.ClassSource
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeType
import org.spekframework.spek2.runtime.scope.TestScopeImpl
import java.util.*

class SpekTestDescriptor internal constructor(
        val scope: ScopeImpl,
        private val factory: SpekTestDescriptorFactory
) : TestDescriptor {

    companion object {
        private val ENGINE_ID = UniqueId.forEngine(SpekTestEngine.ID)
    }

    private val id by lazy { computeId(scope) }

    private fun computeId(scope: ScopeImpl?): UniqueId = if (scope == null) {
        ENGINE_ID
    } else {
        computeId(scope.parent as ScopeImpl?).append("${scope.id.type}", scope.id.name)
    }

    private var engineDescriptor: SpekEngineDescriptor? = null
    private val childDescriptors = mutableSetOf<TestDescriptor>()

    override fun getUniqueId() = id

    override fun getDisplayName(): String = scope.path.name

    override fun getType(): TestDescriptor.Type = when (scope) {
        is GroupScopeImpl -> TestDescriptor.Type.CONTAINER
        is TestScopeImpl -> TestDescriptor.Type.TEST
    }

    override fun getSource(): Optional<TestSource> = when (scope.id.type) {
        ScopeType.Class -> Optional.of(ClassSource.from(scope.id.name))
        ScopeType.Scope -> Optional.empty()
    }

    override fun setParent(parent: TestDescriptor?) {
        // Called only when adding as a child of the engine's descriptor.
        // Will be null only if it's not the root scope.
        if (parent != null && parent is SpekEngineDescriptor) {
            engineDescriptor = parent
        }
    }

    override fun getParent(): Optional<TestDescriptor> {
        val parent = scope.parent as ScopeImpl?

        return Optional.of(
            if (parent != null) {
                factory.create(parent)
            } else {
                // Root scope, setParent(...) was called before.
                engineDescriptor!!
            }
        )
    }

    override fun addChild(descriptor: TestDescriptor) {
        childDescriptors.add(descriptor)
    }

    override fun getChildren() = childDescriptors

    override fun getTags(): MutableSet<TestTag> = mutableSetOf()

    override fun removeFromHierarchy() {
        parent.ifPresent { parent ->
            parent.removeChild(this)
        }
    }

    override fun removeChild(descriptor: TestDescriptor) {
        if (scope is GroupScopeImpl) {
            childDescriptors.remove(descriptor)
            scope.removeChild((descriptor as SpekTestDescriptor).scope)
        }
    }

    override fun findByUniqueId(uniqueId: UniqueId): Optional<out TestDescriptor> =
        throw UnsupportedOperationException()
}
