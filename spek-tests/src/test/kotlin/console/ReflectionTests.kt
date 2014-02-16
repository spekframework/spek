package org.spek.console.test

import org.junit.Test as spec
import kotlin.test.assertEquals
import org.spek.console.findClassesInClassPath
import org.spek.console.getUrlsForPaths
import org.spek.console.findClassesInUrls
import org.spek.console.findSpecs

public class ReflectionTests {

    spec fun findAllClassesForGivenPackage() {

     //   val tests = getUrlsForPaths(listOf("/Users/hadihariri/projects/kotlin/spek/build/production/spek-samples"),"org.spek.samples")
        val tests = findSpecs(listOf("/Users/hadihariri/projects/kotlin/spek/build/production/spek-samples"),"org.spek.samples")
        assertEquals(5, tests.size)
    }

}
