package org.spekframework.spek2.style.specification

import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.Pending
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.meta.*

@SpekDsl
class Suite(delegate: GroupBody): GroupBody by delegate {
    @Synonym(SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun describe(description: String, pending: Pending = Pending.No, body: Suite.() -> Unit) {
        createSuite(description, pending, body)
    }

    @Synonym(SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun context(description: String, pending: Pending = Pending.No, body: Suite.() -> Unit) {
        createSuite(description, pending, body)
    }

    @Synonym(SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xdescribe(description: String, reason: String = "", body: Suite.() -> Unit) {
        createSuite(description, Pending.Yes(reason), body)
    }

    @Synonym(SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xcontext(description: String, reason: String = "", body: Suite.() -> Unit) {
        createSuite(description, Pending.Yes(reason), body)
    }

    @Synonym(SynonymType.TEST)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun it(description: String, pending: Pending = Pending.No, body: TestBody.() -> Unit) {
        createTest(description, pending, body)
    }

    @Synonym(SynonymType.TEST)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xit(description: String, reason: String = "", body: TestBody.() -> Unit) {
        createTest(description, Pending.Yes(reason), body)
    }

    fun before(cb: () -> Unit) {
        beforeGroup(cb)
    }

    fun after(cb: () -> Unit) {
        afterGroup(cb)
    }

    fun beforeEach(cb: () -> Unit) {
        beforeEachTest(cb)
    }

    fun afterEach(cb: () -> Unit) {
        afterEachTest(cb)
    }
}

@Synonym(SynonymType.GROUP)
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.describe(description: String, pending: Pending = Pending.No, body: Suite.() -> Unit) {
    createSuite(description, pending, body)
}

private fun GroupBody.createSuite(description: String, pending: Pending, body: Suite.() -> Unit) {
    group(description, pending) {
        body(Suite(this))
    }
}

private fun GroupBody.createTest(description: String, pending: Pending, body: TestBody.() -> Unit) {
    test(description, pending, body)
}
