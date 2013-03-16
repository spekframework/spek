package org.spek.test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test
import org.spek.api.*
import org.spek.console.reflect.FileClassLoader
import org.spek.junit.api.JUnitSpek

class calculatorSpecs: JUnitSpek() {{
    given("file class loader") {
        on("a package with spek tests") {
            val classes = FileClassLoader.getClasses("org.spek")
            it("should detect test classes") {
                assertEquals(true, classes.size > 0)
            }
            it("should only find Spek inheritors") {
                classes forEach { assertTrue(javaClass<Spek>().isAssignableFrom(it)) }
            }
        }
    }
}}

