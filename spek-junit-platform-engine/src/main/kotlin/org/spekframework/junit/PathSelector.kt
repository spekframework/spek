package org.spekframework.junit

import org.junit.platform.engine.DiscoverySelector
import org.spekframework.runtime.scope.Path

class PathSelector(val path: Path): DiscoverySelector
