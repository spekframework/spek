package org.jetbrains.spek.api

interface DescribeBody  {
    fun describe(description: String, evaluateBody: DescribeBody.() -> Unit)
    fun xdescribe(description: String, evaluateBody: DescribeBody.() -> Unit)
    fun fdescribe(description: String, evaluateBody: DescribeBody.() -> Unit)

    fun it(description: String, assertions: AssertionBody.() -> Unit)
    fun xit(description: String, assertions: AssertionBody.() -> Unit)
    fun fit(description: String, assertions: AssertionBody.() -> Unit)

    fun beforeEach(actions: AssertionBody.() -> Unit)
    fun afterEach(actions: () -> Unit)

    fun on(description: String, evaluateBody: DescribeBody.() -> Unit) = describe(description, evaluateBody)
    fun given(description: String, evaluateBody: DescribeBody.() -> Unit) = describe(description, evaluateBody)
    fun context(description: String, evaluateBody: DescribeBody.() -> Unit) = describe(description, evaluateBody)
    fun xon(description: String, evaluateBody: DescribeBody.() -> Unit) = xdescribe(description, evaluateBody)
    fun xgiven(description: String, evaluateBody: DescribeBody.() -> Unit) = xdescribe(description, evaluateBody)
    fun xcontext(description: String, evaluateBody: DescribeBody.() -> Unit) = xdescribe(description, evaluateBody)
    fun fon(description: String, evaluateBody: DescribeBody.() -> Unit) = fdescribe(description, evaluateBody)
    fun fgiven(description: String, evaluateBody: DescribeBody.() -> Unit) = fdescribe(description, evaluateBody)
    fun fcontext(description: String, evaluateBody: DescribeBody.() -> Unit) = fdescribe(description, evaluateBody)
}