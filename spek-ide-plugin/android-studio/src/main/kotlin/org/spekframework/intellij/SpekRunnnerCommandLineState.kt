package org.spekframework.intellij

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.application.BaseJavaApplicationCommandLineState
import com.intellij.execution.configurations.JavaParameters
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.execution.testframework.sm.runner.SMTestLocator
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.util.JavaParametersUtil
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.util.PathUtil
import org.spekframework.ide.ServiceMessageAdapter

class SpekRunnnerCommandLineState<T>(environment: ExecutionEnvironment, configuration: T)
    : BaseJavaApplicationCommandLineState<T>(environment, configuration)
where T: RunConfigurationBase,
      T: SpekBaseJvmRunConfiguration {
    override fun createJavaParameters(): JavaParameters {
        val params = JavaParameters()
        params.isUseClasspathJar = true
        val module = myConfiguration.configurationModule
        val jreHome = if (myConfiguration.isAlternativeJrePathEnabled) {
            myConfiguration.alternativeJrePath
        } else {
            null
        }

        val pathType = JavaParameters.JDK_AND_CLASSES_AND_TESTS
        JavaParametersUtil.configureModule(module, params, pathType, jreHome)

        val jars = listOf(
            PathUtil.getJarPathForClass(ServiceMessageAdapter::class.java)
        )

        params.classPath.addAll(jars)
        params.mainClass = "org.spekframework.ide.ConsoleKt"

        setupJavaParameters(params)

        Extensions.getExtensions(SpekJvmParameterPatcher.PARAMETER_PATCHER_EP).forEach {
            it.patch(module.module!!, params)
        }

        module.module?.let {
            OrderEnumerator.orderEntries(it)
                .withoutLibraries()
                .withoutDepModules()
                .withoutSdk()
                .recursively()
                .classes()
                .pathsList
                .pathList
                .forEach {
                    params.programParametersList.add("--sourceDirs", it)
                }

        }

        params.programParametersList.add("--paths", configuration.data.path.serialize())


        return params
    }

    fun createConsole(executor: Executor, processHandler: ProcessHandler): ConsoleView {
        val consoleProperties = object: SMTRunnerConsoleProperties(
            myConfiguration, "spek2", executor
        ) {
            override fun getTestLocator(): SMTestLocator? {
                return SpekSMTestLocator
            }
        }
        return SMTestRunnerConnectionUtil.createAndAttachConsole(
            "spek2",
            processHandler,
            consoleProperties
        )
    }

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        val processHandler = startProcess()
        val console = createConsole(executor, processHandler)
        return DefaultExecutionResult(
            console, processHandler, *createActions(console, processHandler, executor)
        )
    }
}