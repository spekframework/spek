package org.jetbrains.spek.api

import org.jetbrains.spek.junit.*
import org.junit.runner.*

@RunWith(JUnitSpekRunner::class)
open class Spek(val spekBody: DescribeBody.() -> Unit) {
    val parentDescribeBody = DescribeTreeGenerator()

    init {
        parentDescribeBody.describe(this.javaClass.simpleName, spekBody)
    }

    fun testAction(): TestAction {
        return parentDescribeBody.recordedActions()[0]
    }

    fun run(notifier: Notifier) {
        parentDescribeBody.recordedActions()[0].run(notifier)
    }
}
