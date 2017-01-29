package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.CreateWith
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.lifecycle.InstanceFactory
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

/**
 * @author Ranie Jade Ramiso
 */
class InstanceFactoryTest: AbstractSpekTestEngineTest() {
    object SimpleFactoryAsAnObject: InstanceFactory {
        override fun <T: Spek> create(spek: KClass<T>): T {
            return spek.objectInstance ?: spek.primaryConstructor!!.call()
        }
    }

    class SimpleFactoryAsAClass: InstanceFactory {
        override fun <T: Spek> create(spek: KClass<T>): T {
            return spek.objectInstance ?: spek.primaryConstructor!!.call()
        }
    }

    @Test
    fun testDefaultUsingSecondaryConstructors() {
        class SomeSpec(number: Int): Spek({
            it("should be $number") { }
        }) {
            constructor(): this(10)
        }

        val recorder = executeTestsForClass(SomeSpec::class)
        assertThat(recorder.testSuccessfulCount, equalTo(1))
    }

    @Test
    fun testUsingObject() {
        @CreateWith(SimpleFactoryAsAnObject::class)
        class SomeSpec: Spek({
            it("should work") {

            }
        })

        val recorder = executeTestsForClass(SomeSpec::class)
        assertThat(recorder.testSuccessfulCount, equalTo(1))
    }

    @Test
    fun testUsingClass() {
        @CreateWith(SimpleFactoryAsAClass::class)
        class SomeSpec: Spek({
            it("should work") {

            }
        })

        val recorder = executeTestsForClass(SomeSpec::class)
        assertThat(recorder.testSuccessfulCount, equalTo(1))
    }
}
