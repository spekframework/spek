package org.spek.junit.api

import org.spek.api.*
import org.spek.impl.*
import org.spek.junit.impl.*
import org.junit.runner.RunWith
import org.junit.Test as test

RunWith(javaClass<JSpec<*>>())
public abstract class JUnitSpek: Spek {

    private val spekImpl = SpekImpl()

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        spekImpl.given(description, givenExpression)
    }

    fun allGivens(): List<TestGivenAction> = spekImpl.allGivens()

    //possible workaround to cheat JUnit integration
    test public fun mockTest() {
    }
}
