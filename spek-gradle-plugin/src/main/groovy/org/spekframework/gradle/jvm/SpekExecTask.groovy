package org.spekframework.gradle.jvm

import org.gradle.api.tasks.JavaExec

class SpekExecTask extends JavaExec {
    SpekExecTask() {
        description = "Run Spek specs."
        main = "org.spekframework.jvm.JvmConsoleRunnerKt"
    }
}
