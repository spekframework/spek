package org.jetbrains.spek.console

import kotlin.collections.listOf
import org.junit.Test as test
import kotlin.test.assertEquals

class ConsoleRunnerTest {
    @test fun getEmptyOptions() {
        //given an empty array
        //when we call getOptions
        val options = getOptions(arrayOf<String>())
        //then
        assertEquals(listOf<String>(), options.paths)
        assertEquals("", options.packageName)
        assertEquals("", options.filename)
        assertEquals("", options.cssFile)
        assertEquals("text", options.format)
    }

    @test fun getNonEmptyOptions() {
        //given an empty array
        //when we call getOptions
        val options = getOptions(arrayOf("paths", "my.package", "-f", "text", "--output", "file.txt", "--css", "css.css"))
        //then
        assertEquals(listOf("paths"), options.paths)
        assertEquals("my.package", options.packageName)
        assertEquals("file.txt", options.filename)
        assertEquals("css.css", options.cssFile)
        assertEquals("text", options.format)
    }
}