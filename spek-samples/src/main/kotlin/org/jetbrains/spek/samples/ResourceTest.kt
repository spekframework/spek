package org.jetbrains.spek.samples

import org.jetbrains.spek.api.Spek
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class ResourceTest : Spek({

    given("a closeable resource opened in a test") {

        it("should be closed if the test succeeds") {
            val baos = ByteArrayOutputStream()
            baos.autoCleanup()

            assertEquals(42, 42)
        }

        it("should be closed if the test fails") {
            val baos = ByteArrayOutputStream()
            baos.autoCleanup()

            assertEquals(42, 21)
        }

    }

})

