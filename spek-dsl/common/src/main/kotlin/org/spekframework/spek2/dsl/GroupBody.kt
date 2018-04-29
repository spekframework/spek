package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.meta.*

/**
 * @since 1.0
 */
@SpekDsl
interface GroupBody: TestContainer {
    @Synonym(type = SynonymType.GROUP)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun group(description: String, pending: Pending = Pending.No, body: GroupBody.() -> Unit)

    @Synonym(type = SynonymType.ACTION)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun action(description: String, pending: Pending = Pending.No, body: ActionBody.() -> Unit)

    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T): LifecycleAware<T>
    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T, destructor: (T) -> Unit): LifecycleAware<T>

    fun beforeEachTest(callback: () -> Unit)
    fun afterEachTest(callback: () -> Unit)

    fun beforeGroup(callback: () -> Unit)
    fun afterGroup(callback: () -> Unit)

    /**
     * Creates a [group][GroupBody.group].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.GROUP, prefix = "describe")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun describe(description: String, body: GroupBody.() -> Unit) {
        group("describe $description", body = body)
    }

    /**
     * Creates a [group][GroupBody.group].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.GROUP, prefix = "context")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun context(description: String, body: GroupBody.() -> Unit) {
        group("context $description", body = body)
    }

    /**
     * Creates a [group][GroupBody.group].
     *
     * @since 1.0
     */

    @Synonym(type = SynonymType.GROUP, prefix = "given")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun given(description: String, body: GroupBody.() -> Unit) {
        group("given $description", body = body)
    }

    /**
     * Creates an [action][GroupBody.action].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.ACTION, prefix = "on")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun on(description: String, body: ActionBody.() -> Unit) {
        action("on $description", body = body)
    }

    /**
     * Creates a [group][GroupBody.group].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.GROUP, prefix = "describe", excluded = true)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xdescribe(description: String, reason: String? = null, body: GroupBody.() -> Unit) {
        group("describe $description", Pending.Yes(reason), body = body)
    }

    /**
     * Creates a [group][GroupBody.group].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.GROUP, prefix = "context", excluded = true)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xcontext(description: String, reason: String? = null, body: GroupBody.() -> Unit) {
        group("context $description", Pending.Yes(reason), body = body)
    }

    /**
     * Creates a [group][GroupBody.group].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.GROUP, prefix = "given", excluded = true)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xgiven(description: String, reason: String? = null, body: GroupBody.() -> Unit) {
        group("given $description", Pending.Yes(reason), body = body)
    }

    /**
     * Creates a pending [action][GroupBody.action].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.ACTION, prefix = "on", excluded = true)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xon(description: String, reason: String? = null, body: ActionBody.() -> Unit = {}) {
        action("on $description", Pending.Yes(reason), body = body)
    }
}
