package org.spekframework.spek2.junit

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.sameInstance
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.ScopeId
import org.spekframework.spek2.runtime.scope.ScopeType
import kotlin.properties.Delegates

class SpekTestDescriptorFactoryTest {

    var factory: SpekTestDescriptorFactory by Delegates.notNull()
    var lifecycleManager: LifecycleManager by Delegates.notNull()

    @BeforeEach
    fun setup() {
        factory = SpekTestDescriptorFactory()
        lifecycleManager = mock()
    }

    @Test
    fun caching() {
        val path = PathBuilder()
            .append("SomeClass")
            .build()

        val scope = GroupScopeImpl(
            ScopeId(ScopeType.Class, "SomeClass"),
            path,
            null,
            Skip.No,
            lifecycleManager,
            false
        )

        assertThat(factory.create(scope), sameInstance(factory.create(scope)))
    }
}
