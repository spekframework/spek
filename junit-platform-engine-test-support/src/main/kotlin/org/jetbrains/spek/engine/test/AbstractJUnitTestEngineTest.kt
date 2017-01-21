package org.jetbrains.spek.engine.test

import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.discovery.DiscoverySelectors
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractJUnitTestEngineTest<out T: TestEngine>(protected val engine: T) {

    protected fun executeTestsForClass(testClass: KClass<*>): ExecutionEventRecorder {
        val request = LauncherDiscoveryRequestBuilder.request()
        return executeTests(request.selectors(DiscoverySelectors.selectClass(testClass.java)).build())
    }

    protected fun executeForPackage(`package`: String): ExecutionEventRecorder {
        val request = LauncherDiscoveryRequestBuilder.request()
        return executeTests(
            request.selectors(DiscoverySelectors.selectPackage(`package`))
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
