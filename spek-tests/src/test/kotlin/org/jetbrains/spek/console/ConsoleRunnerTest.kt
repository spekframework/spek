package org.jetbrains.spek.console

import kotlin.test.assertEquals
import org.junit.Test as test

class ConsoleRunnerTest {
    @test fun getEmptyOptions() {
        val options = getOptions(arrayOf<String>())

        assertEquals(listOf<String>(), options.paths)
        assertEquals("", options.packageName)
        assertEquals("", options.filename)
        assertEquals("text", options.format)
    }

    @test fun htmlOptionDefaultsToFile() {
        val options = getOptions(arrayOf("paths", "my.package", "-f", "html"))

        assertEquals(listOf("paths"), options.paths)
        assertEquals("my.package", options.packageName)
        assertEquals("out.html", options.filename)
        assertEquals("html", options.format)
    }

    @test fun htmlOptionAllowsOverridingFilename() {
        val options = getOptions(arrayOf("paths", "my.package", "-f", "html", "-o", "file.html"))

        assertEquals(listOf("paths"), options.paths)
        assertEquals("my.package", options.packageName)
        assertEquals("file.html", options.filename)
        assertEquals("html", options.format)
    }

    @test fun textOptionAllowsSettingFilename() {
        val options = getOptions(arrayOf("paths", "my.package", "-f", "text", "--output", "file.txt"))

        assertEquals(listOf("paths"), options.paths)
        assertEquals("my.package", options.packageName)
        assertEquals("file.txt", options.filename)
        assertEquals("text", options.format)
    }
}