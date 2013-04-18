package org.spek.console.cmd

import org.junit.Test as test
import kotlin.test.assertEquals

public class ConsoleRunnerTest {
    test fun getEmptyOptions() {
        //given an empty array
        //when we call getOptions
        val options = getOptions(array())
        //then
        assertEquals("", options.packageName)
        assertEquals("", options.filename)
        assertEquals("", options.cssFile)
        assertEquals(false, options.toText)
        assertEquals(false, options.toHtml)
    }

    test fun getOptions() {
        //given an empty array
        //when we call getOptions
        val options = getOptions(array("my.package", "-text", "-html", "-file", "file.txt", "-css", "css.css"))
        //then
        assertEquals("my.package", options.packageName)
        assertEquals("file.txt", options.filename)
        assertEquals("css.css", options.cssFile)
        assertEquals(true, options.toText)
        assertEquals(true, options.toHtml)
    }
}