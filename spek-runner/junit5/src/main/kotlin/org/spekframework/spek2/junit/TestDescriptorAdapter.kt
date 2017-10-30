package org.spekframework.spek2.junit

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestSource
import org.junit.platform.engine.TestTag
import org.junit.platform.engine.UniqueId
import org.spekframework.spek2.runtime.scope.ActionScopeImpl
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import java.util.Optional

class TestDescriptorAdapter internal constructor(val scope: ScopeImpl,
                                                 val factory: TestDescriptorAdapterFactory): TestDescriptor {

    private val _uniqueId by lazy {
        toUniqueId(scope)
    }

    private var rootEngineDescriptor: SpekEngineDescriptor? = null

    private val _children = mutableSetOf<TestDescriptor>()

    override fun getSource(): Optional<TestSource> {
        return Optional.empty()
    }

    override fun removeFromHierarchy() {
        throw UnsupportedOperationException()
    }

    override fun setParent(parent: TestDescriptor?) {
        // this only set when adding as a child of the engine's descriptor
        // will only be null if it's not the root scope
        if (parent != null && parent is SpekEngineDescriptor) {
            rootEngineDescriptor = parent
        }
    }

    override fun getParent(): Optional<TestDescriptor> {
        val parent = if (scope is GroupScopeImpl) {
            scope.parent
        } else {
            // TestScope which always have a parent
            scope.parent!!
        } as ScopeImpl?

        return if (parent != null) {
            Optional.of(factory.create(parent))
        } else {
            // root scope, setParent(...) is called.
            Optional.of(rootEngineDescriptor!!)
        }
    }

    override fun getChildren() = _children

    override fun getDisplayName(): String {
        return scope.path.name
    }

    override fun getType(): TestDescriptor.Type {
        return when (scope) {
            is ActionScopeImpl -> TestDescriptor.Type.CONTAINER_AND_TEST
            is GroupScopeImpl -> TestDescriptor.Type.CONTAINER
            else -> TestDescriptor.Type.TEST
        }
    }

    override fun mayRegisterTests(): Boolean {
        return scope is ActionScopeImpl
    }

    override fun getUniqueId() = _uniqueId

    override fun removeChild(descriptor: TestDescriptor) {
        throw UnsupportedOperationException()
    }

    override fun addChild(descriptor: TestDescriptor) {
        _children.add(descriptor)
    }

    override fun findByUniqueId(uniqueId: UniqueId): Optional<out TestDescriptor> {
        throw UnsupportedOperationException()
    }

    override fun getTags(): MutableSet<TestTag> {
        return mutableSetOf()
    }
}
