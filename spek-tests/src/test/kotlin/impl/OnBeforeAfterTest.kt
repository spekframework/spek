package org.spek.test

import org.junit.Test as test
import org.spek.console.*
import kotlin.test.*
import org.junit.*
import org.spek.*
import org.spek.test.IntegrationTestCase.Data

public class OnBeforeAfterTest : IntegrationTestCase() {

    test public fun callOnBefore() {
        val log = arrayListOf<String>()
        runTest(object : Data() {{
          given("a") {
              beforeOn { log add "before"  }
              afterOn { log add "after"  }

              on("1") {
                  it("ddd") {}
                  log add "on"
              }
              on("2") { it("zzz") { log add "it"; fail("rrr") } }
          }
        }})

        Assert.assertEquals(arrayListOf("before", "on", "after", "before", "it", "after"), log)
    }
}