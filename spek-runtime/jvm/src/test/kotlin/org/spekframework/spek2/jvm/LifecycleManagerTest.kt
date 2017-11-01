package org.spekframework.spek2.jvm

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import kotlin.properties.Delegates

/**
 * @author Ranie Jade Ramiso
 */
class LifecycleManagerTest {
    var lifecycleManager by Delegates.notNull<LifecycleManager>()

    @BeforeEach
    fun setup() {
        lifecycleManager = LifecycleManager()
    }

    @Test
    fun sameListenerRegistration() {
        val listener = object: LifecycleListener { }
        lifecycleManager.addListener(listener)

        try {
            lifecycleManager.addListener(listener)
            Assertions.fail<Any>("it should throw IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // pass
        }
    }
}
