package org.spekframework.junit

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.engine.test.ExecutionEventRecorder
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.spekframework.jvm.classToPath
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractSpekTestEngineTest {
    val engine = SpekTestEngine()

    protected fun executeTestsForClass(spek: KClass<out Spek>): ExecutionEventRecorder {
        val request = LauncherDiscoveryRequestBuilder.request()
        return executeTests(
            request.selectors(PathSelector(classToPath(spek)))
                .build()
        )
    }

    protected fun executeTests(request: LauncherDiscoveryRequest): ExecutionEventRecorder {
        val descriptor = discoverTests(request)
        val eventRecorder = ExecutionEventRecorder()
        engine.execute(ExecutionRequest(descriptor, eventRecorder, request.configurationParameters))
        return eventRecorder
    }

    protected fun discoverTests(request: LauncherDiscoveryRequest): TestDescriptor {
        return engine.discover(request, UniqueId.forEngine(engine.id))
    }
}
