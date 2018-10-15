package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.meta.SpekDsl
import org.spekframework.spek2.meta.Synonym
import org.spekframework.spek2.meta.SynonymType

/**
 * @since 1.0
 */
@SpekDsl
interface SpecBody: TestContainer {
    @Synonym(type = SynonymType.Group)
    fun group(description: String, pending: Pending = Pending.No, body: SpecBody.() -> Unit)

    @Synonym(type = SynonymType.Action)
    fun action(description: String, pending: Pending = Pending.No, body: ActionBody.() -> Unit)

    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T): LifecycleAware<T>
    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T, destructor: (T) -> Unit): LifecycleAware<T>

    fun beforeEachTest(callback: () -> Unit)
    fun afterEachTest(callback: () -> Unit)

    fun beforeGroup(callback: () -> Unit)
    fun afterGroup(callback: () -> Unit)

    /**
     * Creates a [group][SpecBody.group].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.Group, prefix = "given")
    fun given(description: String, body: SpecBody.() -> Unit) {
        group("given $description", body = body)
    }

    /**
     * Creates an [action][SpecBody.action].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.Action, prefix = "on")
    fun on(description: String, body: ActionBody.() -> Unit) {
        action("on $description", body = body)
    }

    /**
     * Creates a [group][SpecBody.group].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.Group, prefix = "given", excluded = true)
    fun xgiven(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
        group("given $description", Pending.Yes(reason), body = body)
    }

    /**
     * Creates a pending [action][SpecBody.action].
     *
     * @since 1.0
     */
    @Synonym(type = SynonymType.Action, prefix = "on", excluded = true)
    fun xon(description: String, reason: String? = null, body: ActionBody.() -> Unit = {}) {
        action("on $description", Pending.Yes(reason), body = body)
    }
}
