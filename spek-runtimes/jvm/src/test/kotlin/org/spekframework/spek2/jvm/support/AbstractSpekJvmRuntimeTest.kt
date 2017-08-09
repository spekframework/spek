package org.spekframework.spek2.jvm.support

import org.spekframework.spek2.Spek
import org.spekframework.spek2.jvm.SpekJvmRuntime
import org.spekframework.spek2.jvm.classToPath
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest
import kotlin.reflect.KClass

abstract class AbstractSpekJvmRuntimeTest: AbstractSpekRuntimeTest<SpekJvmRuntime>(SpekJvmRuntime()) {
    override fun toPath(spek: KClass<out Spek>): Path {
        return classToPath(spek)
    }
}
