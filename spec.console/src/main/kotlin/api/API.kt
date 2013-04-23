package org.spek.console.api

import org.spek.api.*
import org.spek.impl.*

public abstract class ConsoleSpek: Spek {

    private val spekImpl = SpekImpl()

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        spekImpl.given(description, givenExpression)
    }

    fun allGivens(): List<TestGivenAction> = spekImpl.allGivens()
}
