package org.jetbrains.spek.console

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConsoleRunnerTest : Spek ({
    describe("getOptions") {
        it("defaults to text if no format is specified") {
            val options = getOptions(arrayOf<String>())

            assertEquals(listOf<String>(), options.paths)
            assertEquals("", options.packageName)
            assertEquals("", options.filename)
            assertEquals("text", options.format)
            assertEquals(false, options.verbose)
        }

        it("defaults to console output when text is selected format") {
            val options = getOptions(arrayOf("paths", "my.package", "-f", "text"))

            assertEquals(listOf("paths"), options.paths)
            assertEquals("my.package", options.packageName)
            assertEquals("", options.filename)
            assertEquals("text", options.format)
        }

        it("allows setting a filename when text is selected format") {
            val options = getOptions(arrayOf("paths", "my.package", "-f", "text", "--output", "file.txt"))

            assertEquals(listOf("paths"), options.paths)
            assertEquals("my.package", options.packageName)
            assertEquals("file.txt", options.filename)
            assertEquals("text", options.format)
        }

        it("defaults to file output when html is selected as the format") {
            val options = getOptions(arrayOf("paths", "my.package", "-f", "html"))

            assertEquals(listOf("paths"), options.paths)
            assertEquals("my.package", options.packageName)
            assertEquals("out.html", options.filename)
            assertEquals("html", options.format)
        }

        it("allows overriding the default filename when html is the selected format") {
            val options = getOptions(arrayOf("paths", "my.package", "-f", "html", "-o", "file.html"))

            assertEquals(listOf("paths"), options.paths)
            assertEquals("my.package", options.packageName)
            assertEquals("file.html", options.filename)
            assertEquals("html", options.format)
        }

        it("sets the verbose flag to true if -v is specified") {
            val options = getOptions(arrayOf("paths", "my.package", "-v", "-o", "file.html"))
            assertEquals(true, options.verbose)
        }
    }

    describe("setupRunner") {
        context("with text output and verbosity true") {
            val options = Options(listOf(""), "", "text", "", true)

            it("uses the OutputDeviceVerboseNotifier") {
                val result = setupRunner(options)
                val notifier = result.notifier as CompositeNotifier
                assertTrue(notifier.notifiers.any { it is OutputDeviceVerboseNotifier })
            }
        }

        context("with text output and verbosity false") {
            val options = Options(listOf(""), "", "text", "", false)

            it("uses the non-verbose OutputDeviceNotifier") {
                val result = setupRunner(options)
                val notifier = result.notifier as CompositeNotifier
                assertTrue(notifier.notifiers.any { it is OutputDeviceNotifier })
                assertFalse(notifier.notifiers.any { it is OutputDeviceVerboseNotifier })
            }
        }
    }
})