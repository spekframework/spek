package org.spekframework.spek2.runtime.discovery

import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import kotlin.reflect.KClass


class TestInfo(val path: Path, private val factory: () -> Spek) {
    fun createInstance(): Spek {
        return factory()
    }
}

class DiscoveryContext(private val testInfos: List<TestInfo>) {
    fun getTests(): List<TestInfo> {
        return testInfos
    }

    companion object {
        fun builder(): DiscoveryContextBuilder = DiscoveryContextBuilder()
    }
}

class DiscoveryContextBuilder {
    private val classInfos = mutableListOf<TestInfo>()

    fun addTest(cls: KClass<out Spek>, factory: () -> Spek): DiscoveryContextBuilder {
        val path = PathBuilder.from(cls)
            .build()
        classInfos.add(TestInfo(path, factory))
        return this
    }

    inline fun<reified T: Spek> addTest(noinline factory: () -> T): DiscoveryContextBuilder {
        return addTest(T::class, factory)
    }

    fun build(): DiscoveryContext = DiscoveryContext(classInfos.toList())
}
