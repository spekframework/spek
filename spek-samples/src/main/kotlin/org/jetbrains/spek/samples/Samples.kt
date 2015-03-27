package org.jetbrains.spek.samples

import org.jetbrains.spek.api.Spek

class SampleCalculator {
    fun sum(x: Int, y: Int) = x + y
    fun subtract(x: Int, y: Int) = x - y
}

class SampleIncUtil {
    fun incValueBy(value: Int, inc: Int) = value + inc
}

class ba: Spek() { {

    given("abc") {
        beforeOn { println("before") }
        on("def") {
            println("on")
            it("should") {
                println("it")
            }
        }
        afterOn { println("after")}
    }
}}