package org.jetbrains.spek.api

import org.jetbrains.spek.junit.JUnitSpekRunner
import org.junit.runner.RunWith

@RunWith(JUnitSpekRunner::class)
open class Spek(val spekBody: DescribeBody.() -> Unit) {
    val tree: SpekTree
    val paths: Set<List<Int>>

    init {
        val parentDescribeBody = DescribeParser()
        parentDescribeBody.describe(this.javaClass.simpleName, spekBody)
        tree = parentDescribeBody.children()[0]
        paths = tree.getPaths()
    }

    fun run(notifier: Notifier) {
        paths.forEach { path ->
            tree.runPath(path, notifier)
        }
    }
}