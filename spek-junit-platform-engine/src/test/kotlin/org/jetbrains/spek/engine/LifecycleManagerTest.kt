package org.jetbrains.spek.engine

import org.jetbrains.spek.api.lifecycle.LifecycleListener
import org.jetbrains.spek.engine.lifecycle.LifecycleManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
            Assertions.fail("it should throw IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // pass
        }
    }
}
