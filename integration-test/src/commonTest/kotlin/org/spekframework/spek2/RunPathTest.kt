package org.spekframework.spek2

import org.spekframework.spek2.runtime.scope.RunPath
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

object RunPathTest: Spek({
    describe("RunPath") {
        it("should parse root") {
            RunPath("/")
        }

        it("should parse valid paths") {
            RunPath("/com/example/FooSpec/Some foo/do this")
        }

        it("should throw exception when parsing invalid paths") {
            assertFailsWith<IllegalArgumentException> {
                RunPath("com/example/FooSpec")
            }
        }

        it("should check for ancestry") {
            listOf(
                "/",
                "/com",
                "/com/example",
                "/com/example/FooSpec",
                "/com/example/FooSpec/Some foo",
            ).map(::RunPath).forEach { path ->
                assertTrue {
                    path.isParentOf(RunPath("/com/example/FooSpec/Some foo/do this"))
                }
            }
        }
    }
})