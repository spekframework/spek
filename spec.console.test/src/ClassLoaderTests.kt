package org.spek.test

import kotlin.test.assertNotNull
import org.junit.Test as test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.spek.console.reflect.FileClassLoader
import kotlin.test.assertTrue
import org.spek.api.Spek


public class FileClassLoaderTests {
    test fun given_a_valid_folder_and_package_name_should_return_list_of_classes() {
        val classes = FileClassLoader.getClasses("org.spek")

        assertEquals(true, classes.size > 0)
        classes forEach { assertTrue(javaClass<Spek>().isAssignableFrom(it) ) }
    }
}
