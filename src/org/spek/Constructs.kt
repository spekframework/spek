package org.spek

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.test.assertEquals
import org.spek.junit.JSpec
import org.junit.runner.RunWith

Retention(RetentionPolicy.RUNTIME) annotation class spec

val eventAggregator = SimpleEventAggregator()

public fun given(description : String, givenExpression: Given.() -> Unit) : TestAction {
    return object:TestAction {
        override val Description: String = if (description.length() == 0) "Given" else  description

        override fun run() {
            Given().givenExpression()
        }
    }
}

public trait TestAction {
    val Description : String
    fun run()
}

test
RunWith(javaClass<JSpec<*>>())
public abstract class Spek(vararg val actions : TestAction){
    test public fun mockTest() {}
}
