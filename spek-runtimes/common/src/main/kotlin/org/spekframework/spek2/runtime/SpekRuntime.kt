package org.spekframework.spek2.runtime

import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Pending
import org.spekframework.spek2.lifecycle.InstanceFactory
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.DiscoveryResult
import org.spekframework.spek2.runtime.execution.ExecutionContext
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.ScopeId
import org.spekframework.spek2.runtime.scope.ScopeType
import kotlin.reflect.KClass

abstract class SpekRuntime {

    abstract fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult

    protected fun resolveSpec(spek: KClass<out Spek>, path: Path): GroupScopeImpl {
        val fixtures = FixturesAdapter()
        val lifecycleManager = LifecycleManager().apply {
            addListener(fixtures)
        }

        val qualifiedName = (path.parent?.name ?: "") + ".${path.name}"
        val classScope = GroupScopeImpl(ScopeId(ScopeType.CLASS, qualifiedName), path, null, Pending.No, lifecycleManager)
        val instanceFactory = instanceFactoryFor(spek)
        val instance = instanceFactory.create(spek)
        instance.spec.invoke(Collector(classScope, lifecycleManager, fixtures))

        return classScope
    }

    protected abstract fun instanceFactoryFor(spek: KClass<*>): InstanceFactory

    fun execute(request: ExecutionRequest) {
        Executor().execute(ExecutionContext(request))
    }
}
