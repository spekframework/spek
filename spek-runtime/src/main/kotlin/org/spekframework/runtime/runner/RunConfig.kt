package org.spekframework.runtime.runner

import org.spekframework.runtime.execution.RuntimeExecutionListener
import org.spekframework.runtime.scope.Path

data class RunConfig(val path: Path,
                     val recorder: RuntimeExecutionListener)
