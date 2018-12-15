package org.spekframework.spek2.junit

import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl

class SpekTestDescriptorFactory {

    private val cache = mutableMapOf<ScopeImpl, SpekTestDescriptor>()

    fun create(scope: ScopeImpl): SpekTestDescriptor = createDescriptor(scope)

    private fun createDescriptor(scope: ScopeImpl) = cache.computeIfAbsent(scope) {
        SpekTestDescriptor(scope, this).apply {
            if (scope is GroupScopeImpl) {
                scope.getChildren().forEach { child ->
                    this.addChild(create(child))
                }
            }
        }
    }
}
