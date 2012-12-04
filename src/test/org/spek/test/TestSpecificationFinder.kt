package org.spek.test

import kotlin.test.*
import org.spek.SpecificationFinder
import org.junit.Test as test

public class TestSpecificationFinder {

    test fun can_create_specification_finder_instance() {

        val specFinder = SpecificationFinder()

        assertNotNull(specFinder)
    }

    test fun getSpecificationsByPackagName_with_empty_string_returns_empty_list() {


        val specFinder = SpecificationFinder()

        val specifications = specFinder.getSpecifications("")

        assertEquals(0, specifications.count())
    }

    test fun getSpecificationsByPackageName_with_valid_package_name_returns_list_of_specifications_in_the_package() {

        val specFinder = SpecificationFinder()

        val specifications = specFinder.getSpecifications("org.spek.test.samples")

        assertEquals(2, specifications.count())

        assertEquals(specifications.first().method.getName(), "calculatorSpecs")
    }

}