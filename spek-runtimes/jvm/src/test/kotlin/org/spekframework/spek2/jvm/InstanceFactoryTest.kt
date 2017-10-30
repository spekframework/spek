package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.CreateWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.InstanceFactory
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * @author Ranie Jade Ramiso
 */
class InstanceFactoryTest: AbstractSpekRuntimeTest() {
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
