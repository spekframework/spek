package org.jetbrains.spek.api

class DescribeExecutor(val index: Int) : DescribeBody {
    val impl = DescribeTreeGenerator()

    var currentIndex = 0
    private fun indexCheck(function: () -> Unit) {
        if (currentIndex == index) {
            function()
        }
        currentIndex++
    }

    override fun describe(description: String, evaluateBody: DescribeBody.() -> Unit) {
        indexCheck { impl.describe(description, evaluateBody) }
    }

    override fun xdescribe(description: String, evaluateBody: DescribeBody.() -> Unit) {
        indexCheck { impl.xdescribe(description, evaluateBody) }
    }

    override fun xit(description: String, assertions: () -> Unit) {
        indexCheck { impl.xit(description, assertions) }
    }

    override fun it(description: String, assertions: () -> Unit) {
        indexCheck { impl.it(description, assertions) }
    }

    override fun afterEach(actions: () -> Unit) {
        impl.afterEach(actions)
    }

    fun run(notifier: Notifier) {
        if (impl.recordedActions().size != 1) {
            throw RuntimeException("Internal error: should only have evaluated one item in the describe body, but evaluated " + impl.recordedActions().size + ": " + this.toString())
        }
        impl.recordedActions()[0].run(notifier)
    }
}