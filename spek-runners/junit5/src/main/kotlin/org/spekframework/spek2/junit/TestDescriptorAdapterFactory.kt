package org.spekframework.spek2.junit

import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl

class TestDescriptorAdapterFactory {
    private val cache = mutableMapOf<ScopeImpl, TestDescriptorAdapter>()

    private fun toDescriptor(scope: ScopeImpl) = cache.computeIfAbsent(scope) {
        TestDescriptorAdapter(scope, this)
    }

    fun create(scope: ScopeImpl): TestDescriptorAdapter {
        return toDescriptor(scope).apply {
            if (scope is GroupScopeImpl) {
                scope.getChildren().forEach { child ->
                    this.addChild(create(child))
                }
            }
        }
    }
}
