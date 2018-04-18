package org.spekframework.spek2.specification

import org.spekframework.spek2.dsl.*
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.meta.Synonym
import org.spekframework.spek2.meta.SynonymType

/**
 * Spek with a Specification style API.
 */
abstract class Spek(body: Specification.() -> Unit): org.spekframework.spek2.Spek({
    body(Specification(this))
})

class Specification(root: Spec): SpecSuite(root)

open class SpecSuite(private val root: SpecBody) {
    /**
     * Provide value to your specs that adheres to
     */
    fun <T> let(mode: CachingMode = CachingMode.TEST, factory: () -> T): LifecycleAware<T> {
        return root.memoized(mode, factory)
    }

    /**
     * Execute block of code before all specs.
     */
    fun beforeAll(callback: () -> Unit) {
        root.beforeGroup(callback)
    }

    /**
     * Execute block of code after all specs.
     */
    fun afterAll(callback: () -> Unit) {
        root.afterGroup(callback)
    }

    /**
     * Execute block of code before each spec.
     */
    fun beforeEach(callback: () -> Unit) {
        root.beforeEachTest(callback)
    }

    /**
     * Execute block of code after each spec.
     */
    fun afterEach(callback: () -> Unit) {
        root.afterEachTest(callback)
    }


    /**
     * A spec suite, contains specs and may nest other spec suites.
     */
    @Synonym(type = SynonymType.Group, prefix = "describe")
    fun describe(description: String, body: SpecSuite.() -> Unit) {
        root.group("describe: $description") {
            body(SpecSuite(this))
        }
    }

    /**
     * An ignored spec suite.
     */
    @Synonym(type = SynonymType.Group, prefix = "describe", excluded = true)
    fun xdescribe(description: String, reason: String = "", body: SpecSuite.() -> Unit) {
        root.group("describe: $description", Pending.Yes(reason)) {
            body(SpecSuite(this))
        }
    }

    /**
     * A spec (or test in JUnit lingo).
     */
    @Synonym(type = SynonymType.Test, prefix = "it")
    fun it(description: String, body: () -> Unit) {
        root.test("it: $description") { body() }
    }

    /**
     * An ignored spec.
     */
    @Synonym(type = SynonymType.Test, prefix = "it", excluded = true)
    fun TestContainer.xit(description: String, reason: String = "", body: () -> Unit) {
        test("it: $description", Pending.Yes(reason)) { body() }
    }
}
