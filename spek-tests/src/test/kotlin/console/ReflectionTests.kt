package console

import org.junit.Test as spec
import org.spek.console.findTestsInPackage
import kotlin.test.assertEquals

public class ReflectionTests {

    spec fun findAllClassesForGivenPackage() {

        val tests = findTestsInPackage("org.spek.sample")

        assertEquals(20, tests.count())
    }

}
