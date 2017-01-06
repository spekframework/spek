package org.jetbrains.spek.api.dsl

import org.jetbrains.spek.meta.SpekDsl

/**
 * @author Ranie Jade Ramiso
 */
@SpekDsl
interface ActionBody: TestContainer {

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun beforeEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun afterEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun beforeGroup(callback: () -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun afterGroup(callback: () -> Unit) {
        throw UnsupportedOperationException()
    }
}
