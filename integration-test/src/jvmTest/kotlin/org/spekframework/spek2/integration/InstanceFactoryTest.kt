package org.spekframework.spek2.integration

import org.spekframework.spek2.CreateWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.InstanceFactory
import kotlin.reflect.KClass
import kotlin.test.assertEquals

const val PARAM_VALUE = 1

object CustomFactory: InstanceFactory {
    override fun <T : Spek> create(spek: KClass<T>): T {
        return spek.constructors.first().call(PARAM_VALUE)
    }
}

@CreateWith(CustomFactory::class)
class InstanceFactoryTest(val parameter: Int): Spek({
    test("parameter should be passed") {
        assertEquals(parameter, PARAM_VALUE)
    }
})
