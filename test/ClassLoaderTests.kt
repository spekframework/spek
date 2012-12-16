package org.spek.test

import kotlin.test.assertNotNull
import org.spek.FileClassLoader
import org.junit.Test as test
import kotlin.test.assertEquals
import kotlin.test.assertNull


public class FileClassLoaderTests {

    test fun can_create_instance_of_FileClassLoader() {
        val classLoader = FileClassLoader()

        assertNotNull(classLoader)
    }

    test fun given_an_emtpy_folder_getClassesByFolder_should_return_empty_array() {
        val classLoader = FileClassLoader()

        val classes = classLoader.getClasses("", "")

        assertEquals(0, classes?.size())
    }

    test fun given_a_valid_folder_and_package_name_should_return_list_of_classes() {
        val classLoader = FileClassLoader()

        val classes = classLoader.getClasses("target/test-classes/org/spek/test/samples", "org.spek.test.samples")

        assertEquals(11, classes?.size())
    }



}