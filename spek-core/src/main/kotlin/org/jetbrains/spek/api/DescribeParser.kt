package org.jetbrains.spek.api

import java.util.*

class DescribeParser() : DescribeBody {
    val befores: LinkedList<AssertionBody.() -> Unit> = LinkedList()
    val afters: LinkedList<() -> Unit> = LinkedList()
    val children = LinkedList<SpekTree>()

    fun children(): List<SpekTree> {
        if (children.isEmpty()) {
            throw RuntimeException(this.javaClass.canonicalName + ": no tests found")
        }
        return children
    }

    override fun describe(description: String, evaluateBody: DescribeBody.() -> Unit) {
        val inner = DescribeParser()
        inner.evaluateBody()

        children.add(SpekTree(
                description,
                ActionType.DESCRIBE,
                SpekStepRunner(befores, afters),
                inner.children))
    }

    override fun xdescribe(description: String, evaluateBody: DescribeBody.() -> Unit) {
        val inner = DescribeParser()
        inner.evaluateBody()

        children.add(SpekTree(
                description,
                ActionType.DESCRIBE,
                SpekIgnoreRunner(),
                listOf()))
    }

    override fun xit(description: String, @Suppress("UNUSED_PARAMETER") assertions: AssertionBody.() -> Unit) {
        children.add(SpekTree(
                "it " + description,
                ActionType.IT,
                SpekIgnoreRunner(),
                listOf()))
    }

    override fun it(description: String, assertions: AssertionBody.() -> Unit) {
        children.add(SpekTree(
                "it " + description,
                ActionType.IT,
                SpekStepRunner(befores, afters, assertions),
                listOf()
        ))
    }

    override fun fdescribe(description: String, evaluateBody: DescribeBody.() -> Unit) {
        val inner = DescribeParser()
        inner.evaluateBody()

        children.add(SpekTree(
                description,
                ActionType.DESCRIBE,
                SpekStepRunner(befores, afters),
                inner.children,
                true
        ))
    }

    override fun fit(description: String, assertions: AssertionBody.() -> Unit) {
        children.add(SpekTree(
                "it " + description,
                ActionType.IT,
                SpekStepRunner(befores, afters, assertions),
                listOf(),
                true
        ))
    }


    override fun beforeEach(actions: AssertionBody.() -> Unit) {
        befores.add(actions)
    }

    override fun afterEach(actions: () -> Unit) {
        afters.add(actions)
    }
}