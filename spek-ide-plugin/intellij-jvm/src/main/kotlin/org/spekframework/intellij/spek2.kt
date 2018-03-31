package org.spekframework.intellij

import com.intellij.execution.CommonJavaRunConfigurationParameters
import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.application.BaseJavaApplicationCommandLineState
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.util.JavaParametersUtil
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.openapi.util.JDOMExternalizerUtil
import com.intellij.util.PathUtil
import org.jdom.Element
import org.spekframework.ide.ConsoleKt
import java.util.*

interface Spek2JvmParameterPatcher {
    fun patch(module: Module, parameters: JavaParameters)

    companion object {
        val PARAMETER_PATCHER_EP = ExtensionPointName.create<Spek2JvmParameterPatcher>("org.spekframework.jvmParameterPatcher")
    }
}

open class Spek2JvmRunConfiguration(name: String,
                                    configurationModule: JavaRunConfigurationModule,
                                    factory: ConfigurationFactory)
    : Spek2BaseRunConfiguration<JavaRunConfigurationModule>(name, configurationModule, factory), CommonJavaRunConfigurationParameters {

    private var alternativeJrePath: String? = ""
    private var vmParameters: String? = ""
    private var alternativeJrePathEnabled = false

    override fun setAlternativeJrePath(path: String?) {
        alternativeJrePath = path
    }

    override fun setVMParameters(value: String?) {
        vmParameters = value
    }

    override fun isAlternativeJrePathEnabled() = alternativeJrePathEnabled

    override fun getPackage() = null

    override fun getRunClass() = null

    override fun setAlternativeJrePathEnabled(enabled: Boolean) {
        alternativeJrePathEnabled = enabled
    }

    override fun getVMParameters() = vmParameters

    override fun getAlternativeJrePath() = alternativeJrePath

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, VM_PARAMETERS, vmParameters)
        JDOMExternalizerUtil.writeField(element, ALTERNATIVE_JRE_PATH, alternativeJrePath)
        JDOMExternalizerUtil.writeField(element, ALTERNATIVE_JRE_PATH_ENABLED, alternativeJrePathEnabled.toString())
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        vmParameters = JDOMExternalizerUtil.readField(element, VM_PARAMETERS)
        alternativeJrePath = JDOMExternalizerUtil.readField(element, ALTERNATIVE_JRE_PATH)
        alternativeJrePathEnabled =
            JDOMExternalizerUtil.readField(element, ALTERNATIVE_JRE_PATH_ENABLED, "false").toBoolean()
    }

    override fun getValidModules(): MutableCollection<Module> =
        Arrays.asList(*ModuleManager.getInstance(project).modules)

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = Spek2JvmSettingsEditor(project)

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return object: BaseJavaApplicationCommandLineState<Spek2JvmRunConfiguration>(environment, this) {
            override fun createJavaParameters(): JavaParameters {
                val params = JavaParameters()
                params.isUseClasspathJar = true
                val module = myConfiguration.configurationModule
                val jreHome = if (myConfiguration.isAlternativeJrePathEnabled) {
                    myConfiguration.alternativeJrePath
                } else {
                    null
                }

                // AndroidJUnitPatcher
                val pathType = JavaParameters.JDK_AND_CLASSES_AND_TESTS
                JavaParametersUtil.configureModule(module, params, pathType, jreHome)

                val jars = mutableListOf(
                    PathUtil.getJarPathForClass(ConsoleKt::class.java)
                )

                params.classPath.addAll(jars)
                params.mainClass = ConsoleKt::class.java.name

                // this will set the vm parameters
                setupJavaParameters(params)

                Extensions.getExtensions(Spek2JvmParameterPatcher.PARAMETER_PATCHER_EP).forEach {
                    it.patch(module.module!!, params)
                }

                module.module?.let {
                    OrderEnumerator.orderEntries(it).recursively().classes()
                        .pathsList.pathList
                        .forEach {
                            params.programParametersList.add("--sourceDirs", it)
                        }

                }

                params.programParametersList.add("--paths", path.serialize())


                return params
            }

            fun createConsole(executor: Executor, processHandler: ProcessHandler): ConsoleView {
                val consoleProperties = SMTRunnerConsoleProperties(
                    this@Spek2JvmRunConfiguration, "spek", executor
                )
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
    }

    companion object {
        const val VM_PARAMETERS = "vmParameters"
        const val ALTERNATIVE_JRE_PATH = "alternativeJrePath"
        const val ALTERNATIVE_JRE_PATH_ENABLED = "alternativeJrePathEnabled"

    }
}
