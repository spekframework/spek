package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.expect

object LetValueSpec : Spek({
    val topLevelValue by value { "top level value" }

    describe("let variables") {
        val str by value { "base" }
        val list by value { mutableListOf<String>() }
        val anotherList by value { mutableListOf<String>() }

        beforeEachTest { anotherList().add("1 ${str()}") }

        it("should permit values to be declared at the top level") {
            expect("top level value") { topLevelValue() }
        }

        it("should memoized the value within a single test") {
            list().add("a string")
            list().add("another string")

            expect(listOf("a string", "another string")) { list() }
        }

        it("should regenerate the value for every test") {
            expect(emptyList<String>()) { list() }
        }

        it("should return the specified value for this context") {
            expect("base") { str() }
        }

        it("should use context values in befores") {
            expect(listOf("1 base")) { anotherList() }
        }

        context("in another context") {
            beforeEachTest { anotherList().add("2A ${str()}") }

            value(str) { "xyz context" }

            beforeEachTest { anotherList().add("2B ${str()}") }

            it("should return the value for that context") {
                expect("xyz context") { str() }
            }

            it("should use context-overridden values in outer befores") {
                expect(listOf("1 xyz context", "2A xyz context", "2B xyz context")) { anotherList() }
            }
        }

        it("should return the correct value after a nested context override") {
            expect("base") { str() }
        }

        context("with nullable values") {
            val nullable by value { null }

            it("can return null") {
                expect(null) { nullable() }
            }
        }
    }
})
