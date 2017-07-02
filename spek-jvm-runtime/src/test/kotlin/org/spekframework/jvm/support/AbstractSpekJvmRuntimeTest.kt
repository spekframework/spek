package org.spekframework.jvm.support

import org.jetbrains.spek.api.Spek
import org.spekframework.jvm.SpekJvmRuntime
import org.spekframework.jvm.classToPath
import org.spekframework.runtime.scope.Path
import org.spekframework.runtime.test.AbstractSpekRuntimeTest
import kotlin.reflect.KClass

abstract class AbstractSpekJvmRuntimeTest: AbstractSpekRuntimeTest<SpekJvmRuntime>(SpekJvmRuntime()) {
    override fun toPath(spek: KClass<out Spek>): Path {
        return classToPath(spek)
    }
}
