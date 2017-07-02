package org.spekframework.runtime

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.lifecycle.InstanceFactory
import org.spekframework.runtime.execution.DiscoveryRequest
import org.spekframework.runtime.execution.DiscoveryResult
import org.spekframework.runtime.execution.ExecutionContext
import org.spekframework.runtime.execution.ExecutionRequest
import org.spekframework.runtime.lifecycle.LifecycleManager
import org.spekframework.runtime.scope.GroupScopeImpl
import org.spekframework.runtime.scope.Path
import kotlin.reflect.KClass

abstract class SpekRuntime {

    abstract fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult

    protected fun resolveSpec(spek: KClass<out Spek>, path: Path): GroupScopeImpl {
        val fixtures = FixturesAdapter()
        val lifecycleManager = LifecycleManager().apply {
            addListener(fixtures)
        }

        val packageScope = path.parent?.let { GroupScopeImpl(it, null, Pending.No, lifecycleManager) }
        val root = GroupScopeImpl(path, packageScope, Pending.No, lifecycleManager)
        val instanceFactory = instanceFactoryFor(spek)
        val instance = instanceFactory.create(spek)
        instance.spec.invoke(Collector(root, lifecycleManager, fixtures))

        return root
    }

    protected abstract fun instanceFactoryFor(spek: KClass<*>): InstanceFactory

    fun execute(request: ExecutionRequest) {
        Executor().execute(ExecutionContext(request))
    }
}
