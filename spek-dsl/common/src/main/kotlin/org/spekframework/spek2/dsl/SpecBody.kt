package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.meta.SpekDsl
import org.spekframework.spek2.meta.Synonym
import org.spekframework.spek2.meta.SynonymType

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@SpekDsl
interface SpecBody: TestContainer {
    @Synonym(type = SynonymType.Group)
    fun group(description: String, pending: Pending = Pending.No, body: SpecBody.() -> Unit)

    @Synonym(type = SynonymType.Action)
    fun action(description: String, pending: Pending = Pending.No, body: ActionBody.() -> Unit)

    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T): LifecycleAware<T>

    fun beforeEachTest(callback: () -> Unit)
    fun afterEachTest(callback: () -> Unit)

    fun beforeGroup(callback: () -> Unit)
    fun afterGroup(callback: () -> Unit)

    /**
     * Creates a [group][SpecBody.group].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Group, prefix = "describe")
    fun describe(description: String, body: SpecBody.() -> Unit) {
        group("describe $description", body = body)
    }

    /**
     * Creates a [group][SpecBody.group].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Group, prefix = "context")
    fun context(description: String, body: SpecBody.() -> Unit) {
        group("context $description", body = body)
    }

    /**
     * Creates a [group][SpecBody.group].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Group, prefix = "given")
    fun given(description: String, body: SpecBody.() -> Unit) {
        group("given $description", body = body)
    }

    /**
     * Creates an [action][SpecBody.action].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Action, prefix = "on")
    fun on(description: String, body: ActionBody.() -> Unit) {
        action("on $description", body = body)
    }

    /**
     * Creates a [group][SpecBody.group].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Group, prefix = "describe", excluded = true)
    fun xdescribe(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
        group("describe $description", Pending.Yes(reason), body = body)
    }

    /**
     * Creates a [group][SpecBody.group].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Group, prefix = "context", excluded = true)
    fun xcontext(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
        group("context $description", Pending.Yes(reason), body = body)
    }

    /**
     * Creates a [group][SpecBody.group].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Group, prefix = "given", excluded = true)
    fun xgiven(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
        group("given $description", Pending.Yes(reason), body = body)
    }

    /**
     * Creates a pending [action][SpecBody.action].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Action, prefix = "on", excluded = true)
    fun xon(description: String, reason: String? = null, body: ActionBody.() -> Unit = {}) {
        action("on $description", Pending.Yes(reason), body = body)
    }
}
