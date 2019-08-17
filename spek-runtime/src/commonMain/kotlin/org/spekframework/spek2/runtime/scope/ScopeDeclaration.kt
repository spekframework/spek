package org.spekframework.spek2.runtime.scope

import kotlinx.coroutines.GlobalScope
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueAdapter2
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KProperty

sealed class ScopeDeclaration {
    enum class FixtureType {
        BEFORE_GROUP,
        AFTER_GROUP,
        BEFORE_EACH_TEST,
        AFTER_EACH_TEST,

        // future types
        BEFORE_EACH_GROUP,
        AFTER_EACH_GROUP
    }
    class Fixture(val type: FixtureType, val cb: () -> Unit): ScopeDeclaration()
    class Memoized<T>(val name: String, val cachingMode: CachingMode, val adapter: MemoizedValueAdapter2<T>): ScopeDeclaration()

    companion object {
        fun beforeGroup(cb: () -> Unit) = Fixture(FixtureType.BEFORE_GROUP, cb)
        fun afterGroup(cb: () -> Unit) = Fixture(FixtureType.AFTER_GROUP, cb)

        fun beforeEachTest(cb: () -> Unit) = Fixture(FixtureType.BEFORE_EACH_TEST, cb)
        fun afterEachTest(cb: () -> Unit) = Fixture(FixtureType.AFTER_EACH_TEST, cb)

        fun <T> memoized(name: String, cachingMode: CachingMode, adapter: MemoizedValueAdapter2<T>) = Memoized(name, cachingMode, adapter)
    }
}

class ExecutionContext(val parent: ExecutionContext?): CoroutineContext.Element {
    private class ValueHolder(val value: Any?)
    private val values = mutableMapOf<String, ValueHolder>()
    override val key: CoroutineContext.Key<*> = Key

    fun <T> getValue(name: String): T {
        if (values.containsKey(name)) {
            return checkNotNull(values[name]).value as T
        } else if (parent != null) {
            return parent.getValue(name)
        }
        throw IllegalArgumentException("No value with name: $name")
    }

    fun <T> setValue(name: String, value: T) {
        values[name] = ValueHolder(value)
    }

    fun <T> clearValue(name: String): T {
        return checkNotNull(values.remove(name)).value as T
    }

    companion object Key: CoroutineContext.Key<ExecutionContext>
}

val CoroutineContext.executionContext: ExecutionContext
    get() {
        return checkNotNull(this[ExecutionContext]) { "Execution context not found!" }
    }

class ScopeValue<T>(private val name: String,
                    private val constructor: () -> T,
                    private val destructor: (T) -> Unit) {
    /*suspend*/ operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        TODO() /* return coroutineContext.executionContext.getValue(name) as T */
    }

    suspend fun init() {
        coroutineContext.executionContext.setValue(name, constructor())
    }

    suspend fun destroy() {
        val value: T = coroutineContext.executionContext.clearValue(name)
        destructor(value)
    }
}

class ScopeValueProvider<T>(private val constructor: () -> T,
                            private val destructor: (T) -> Unit) {
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): ScopeValue<T> {
        return ScopeValue(property.name, constructor, destructor)
    }
}


class Memoized<T>(private val name: String,
                    private val constructor: () -> T,
                    private val destructor: (T) -> Unit) {
    suspend operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return valueFrom(name, coroutineContext)
    }

    // called before a test is executed
    suspend fun init() {
        setValue(name, coroutineContext, constructor())
    }

    // called after a test is executed
    suspend fun destroy() {
        destructor(removeValue(name, coroutineContext))
    }
}
