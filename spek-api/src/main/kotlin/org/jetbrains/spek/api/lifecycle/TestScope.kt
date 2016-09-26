package org.jetbrains.spek.api.lifecycle

import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Experimental
interface TestScope: Scope {
    override val parent: GroupScope
}
