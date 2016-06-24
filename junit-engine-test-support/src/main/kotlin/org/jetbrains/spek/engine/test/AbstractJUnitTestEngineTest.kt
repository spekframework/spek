package org.jetbrains.spek.engine.test

import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.discovery.ClassSelector
import org.junit.platform.launcher.TestDiscoveryRequest
import org.junit.platform.launcher.core.TestDiscoveryRequestBuilder
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractJUnitTestEngineTest<T: TestEngine>(private val engine: T) {

    protected fun executeTestsForClass(testClass: KClass<*>): ExecutionEventRecorder {
        val request = TestDiscoveryRequestBuilder.request()
        return executeTests(request.selectors(ClassSelector.selectClass(testClass.java)).build())
    }

    protected fun executeTests(request: TestDiscoveryRequest): ExecutionEventRecorder {
        val descriptor = discoverTests(request)
        val eventRecorder = ExecutionEventRecorder()
        engine.execute(ExecutionRequest(descriptor, eventRecorder, request.configurationParameters))
        return eventRecorder
    }

    protected fun discoverTests(request: TestDiscoveryRequest): TestDescriptor {
        return engine.discover(request, UniqueId.forEngine(engine.id))
    }
}
