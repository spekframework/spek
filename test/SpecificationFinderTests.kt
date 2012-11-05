package spek.test

import org.junit.Test as test
import kotlin.test.*
import spek.SpecificationFinder


public class SpecificationFinderTests {

    test fun can_create_specification_finder_instance() {

        val specFinder = SpecificationFinder()

        assertNotNull(specFinder)
    }

    test fun getSpecificationsByPackagName_with_empty_string_returns_empty_list() {


        val specFinder = SpecificationFinder()

        val specifications = specFinder.getSpecifications("")

        assertEquals(0, specifications.count())
    }

    test fun getSpecificationsByPackageName_with_valid_packange_name_returns_list_of_specifications_in_the_package() {

        val specFinder = SpecificationFinder()

        val specifications = specFinder.getSpecifications("samples")

        assertEquals(1, specifications.count())

        assertEquals(specifications.first().method.getName(), "calculatorSpecs")
    }

}