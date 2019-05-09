package org.spekframework.spek2.style.specification

import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.LifecycleAware
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.meta.*

@SpekDsl
class Suite(val delegate: GroupBody) : LifecycleAware by delegate {

    var defaultTimeout: Long
        get() = delegate.defaultTimeout
        set(value) { delegate.defaultTimeout = value }

    @Synonym(SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun describe(description: String, skip: Skip = Skip.No, body: Suite.() -> Unit) {
        delegate.createSuite(description, skip, body)
    }

    @Synonym(SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun context(description: String, skip: Skip = Skip.No, body: Suite.() -> Unit) {
        delegate.createSuite(description, skip, body)
    }

    @Synonym(SynonymType.GROUP, excluded = true)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xdescribe(description: String, reason: String = "", body: Suite.() -> Unit) {
        delegate.createSuite(description, Skip.Yes(reason), body)
    }

    @Synonym(SynonymType.GROUP, excluded = true)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xcontext(description: String, reason: String = "", body: Suite.() -> Unit) {
        delegate.createSuite(description, Skip.Yes(reason), body)
    }

    @Synonym(SynonymType.TEST)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun it(description: String, skip: Skip = Skip.No, timeout: Long = delegate.defaultTimeout, body: TestBody.() -> Unit) {
        delegate.createTest(description, skip, timeout, body)
    }

    @Synonym(SynonymType.TEST, excluded = true)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xit(description: String, reason: String = "", timeout: Long = delegate.defaultTimeout, body: TestBody.() -> Unit) {
        delegate.createTest(description, Skip.Yes(reason), timeout, body)
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
fun GroupBody.describe(description: String, skip: Skip = Skip.No, body: Suite.() -> Unit) {
    createSuite(description, skip, body)
}

@Synonym(SynonymType.GROUP)
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.xdescribe(description: String, reason: String = "", body: Suite.() -> Unit) {
    createSuite(description, Skip.Yes(reason), body)
}

private fun GroupBody.createSuite(description: String, skip: Skip, body: Suite.() -> Unit) {
    group(description, skip, CachingMode.TEST) {
        body(Suite(this))
    }
}

private fun GroupBody.createTest(description: String, skip: Skip, timeout: Long, body: TestBody.() -> Unit) {
    test(description, skip, timeout, body)
}
