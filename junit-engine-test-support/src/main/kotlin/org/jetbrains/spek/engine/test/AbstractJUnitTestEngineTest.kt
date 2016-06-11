package org.jetbrains.spek.engine.test

import org.junit.gen5.engine.ExecutionRequest
import org.junit.gen5.engine.TestDescriptor
import org.junit.gen5.engine.TestEngine
import org.junit.gen5.engine.UniqueId
import org.junit.gen5.engine.discovery.ClassSelector
import org.junit.gen5.launcher.TestDiscoveryRequest
import org.junit.gen5.launcher.main.TestDiscoveryRequestBuilder
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
