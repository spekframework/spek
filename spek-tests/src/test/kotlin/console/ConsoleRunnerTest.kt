package org.spek.console.test

import org.junit.Test as test
import kotlin.test.assertEquals
import org.spek.console.*

public class ConsoleRunnerTest {
    test fun getEmptyOptions() {
        //given an empty array
        //when we call getOptions
        val options = getOptions(array())
        //then
        assertEquals(listOf<String>(), options.paths)
        assertEquals("", options.packageName)
        assertEquals("", options.filename)
        assertEquals("", options.cssFile)
        assertEquals("text", options.format)
    }

    test fun getOptions() {
        //given an empty array
        //when we call getOptions
        val options = getOptions(array("paths", "my.package", "-f", "text", "--output", "file.txt", "--css", "css.css"))
        //then
        assertEquals(listOf("paths"), options.paths)
        assertEquals("my.package", options.packageName)
        assertEquals("file.txt", options.filename)
        assertEquals("css.css", options.cssFile)
        assertEquals("text", options.format)
    }
}