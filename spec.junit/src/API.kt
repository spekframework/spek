package org.spek.junit.api

import org.spek.api.*
import org.spek.impl.*
import org.spek.junit.impl.*
import org.junit.runner.RunWith
import junit.framework.TestCase

test
RunWith(javaClass<JSpec<*>>())
public abstract class JUnitSpek : TestCase(), Spek {
    private val givens = arrayListOf<TestGivenAction>()

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        givens.add(givenImpl(description, givenExpression))
    }

    //possible workround to cheat JUnit integration
    test public fun mockTest() {}


    fun allGivens() : List<TestGivenAction> = givens
}
