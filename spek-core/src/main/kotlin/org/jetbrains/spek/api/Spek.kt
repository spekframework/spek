package org.jetbrains.spek.api

import org.jetbrains.spek.junit.JUnitSpekRunner
import org.junit.runner.RunWith

@RunWith(JUnitSpekRunner::class)
open class Spek(val spekBody: DescribeBody.() -> Unit) {
    val tree: SpekTree
    var paths: Set<Path>

    init {
        val parentDescribeBody = DescribeParser()
        parentDescribeBody.describe(this.javaClass.simpleName, spekBody)
        tree = parentDescribeBody.children()[0]
        paths = tree.getPaths()
    }

    fun run(notifier: Notifier) {
        var pathsToRun: List<Path>
        if(tree.focused()) {
            pathsToRun = paths.filter { it.focused }
        } else {
            pathsToRun = paths.toList()
        }
        pathsToRun.forEach { path ->
            tree.runPath(path, notifier)
        }
    }
}