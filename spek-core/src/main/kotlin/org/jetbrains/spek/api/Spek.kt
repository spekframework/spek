package org.jetbrains.spek.api

import org.jetbrains.spek.junit.JUnitSpekRunner
import org.junit.runner.RunWith

@RunWith(JUnitSpekRunner::class)
open class Spek(val spekBody: DescribeBody.() -> Unit) {
    val testAction : TestAction

    init {
        val parentDescribeBody = DescribeParser()
        parentDescribeBody.describe(this.javaClass.simpleName, spekBody)
        testAction = parentDescribeBody.children()[0]
    }

    fun run(notifier: Notifier) {
        testAction.run(notifier, object : SpekContext {
            override fun run(test: () -> Unit) {
                test()
            }
        })
    }
}
