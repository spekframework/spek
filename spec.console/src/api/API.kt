package org.spek.console.api

import org.spek.api.*
import org.spek.impl.*

public abstract class CSpek : Spek {
    private val givens = arrayListOf<TestGivenAction>()

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        givens.add(givenImpl(description, givenExpression))
    }

    fun allGivens() : List<TestGivenAction> = givens
}
