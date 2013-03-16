package org.spek.test

import kotlin.test.assertNotNull
import org.junit.Test as test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.spek.console.reflect.FileClassLoader
import kotlin.test.assertTrue
import org.spek.api.Spek


public class FileClassLoaderTests {
    test fun given_an_emtpy_folder_getClassesByFolder_should_return_empty_array() {
        val classes = FileClassLoader.getClasses("", "")
        assertEquals(0, classes.size())
    }

    test fun given_a_valid_folder_and_package_name_should_return_list_of_classes() {
        val classes = FileClassLoader.getClasses("out/test", "org.spek.test.samples")

        assertEquals(true, classes.size > 0)
        classes forEach { assertTrue(javaClass<Spek>().isAssignableFrom(it) )
        }
    }
}
