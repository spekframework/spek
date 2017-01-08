package org.jetbrains.spek.table

import org.jetbrains.spek.api.dsl.*
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.api.lifecycle.LifecycleListener
import java.util.*

public class CountingTestContainer : TestContainer, Spec {

    override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
        testNames.add(description)
    }

    val testNumber: Int
        get() = testNames.size

    fun reset() {
        testNames.clear()
    }

    val testNames: MutableList<String> = ArrayList()

    override fun beforeGroup(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun afterGroup(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun group(description: String, pending: Pending, body: SpecBody.() -> Unit) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun action(description: String, pending: Pending, body: ActionBody.() -> Unit) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> memoized(mode: CachingMode, factory: () -> T): LifecycleAware<T> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun beforeEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun afterEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun registerListener(listener: LifecycleListener) {
        throw UnsupportedOperationException("not implemented")
    }
}