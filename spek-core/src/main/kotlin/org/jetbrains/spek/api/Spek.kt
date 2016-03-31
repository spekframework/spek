package org.jetbrains.spek.api

import org.jetbrains.spek.junit.JUnitSpekRunner
import org.junit.runner.RunWith

@RunWith(JUnitSpekRunner::class)
open class Spek(val spekBody: DescribeBody.() -> Unit) {
    val tree: SpekTree

    init {
        val parentDescribeBody = DescribeParser()
        parentDescribeBody.describe(this.javaClass.simpleName, spekBody)
        tree = parentDescribeBody.children()[0]
    }

    fun run(notifier: Notifier) {
        tree.run(notifier)
    }
}