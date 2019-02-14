package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.JvmDiscoveryContextFactory
import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import org.spekframework.spek2.runtime.scope.Path

actual class DiscoveryRequest actual constructor(actual val context: DiscoveryContext,
                                                 actual val paths: List<Path>) {
    // TODO: remove on the next release after 2.0.1, it's only here for compatibility
    constructor(testDirs: List<String>, paths: List<Path>): this(JvmDiscoveryContextFactory.create(testDirs), paths)
}