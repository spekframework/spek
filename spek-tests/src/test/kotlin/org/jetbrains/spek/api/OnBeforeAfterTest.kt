package org.jetbrains.spek.api
import org.junit.Test as test
import kotlin.test.*
import org.junit.*
import org.jetbrains.spek.api.IntegrationTestCase.Data

public class OnBeforeAfterTest : IntegrationTestCase() {

    @test public fun callOnBefore() {
        val log = arrayListOf<String>()
        runTest(object: Data() {
            init {
            given("a") {
                beforeOn { log.add("before")}
                on("1") {
                    it("ddd") {}
                    log.add("on")
                }

            }
        }})
        assertEquals(arrayListOf("before", "on"), log)
    }

    @test public fun callOnAfter() {
        val log = arrayListOf<String>()
        runTest(object: Data() {
            init {
            given("a") {
                afterOn { log.add("after")}
                on("1") {
                    it("ddd") {}
                    log.add("on")
                }

            }
        }})
        assertEquals(arrayListOf("on", "after"), log)
    }

    @test public fun callOnBeforeAndOnAfter() {
        val log = arrayListOf<String>()
        runTest(object : Data() {
            init {
            given("a") {
                beforeOn { log.add("before")  }
                afterOn { log.add("after")  }

                on("1") {
                    it("ddd") {}
                    log.add("on")
                }
                on("2") { it("zzz") { log.add("it"); fail("rrr") } }
            }
        }})

        Assert.assertEquals(arrayListOf("before", "on", "after", "before", "it", "after"), log)
    }


}