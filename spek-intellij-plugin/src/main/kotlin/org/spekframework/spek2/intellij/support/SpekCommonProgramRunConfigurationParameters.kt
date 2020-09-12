package org.spekframework.spek2.intellij.support

import com.intellij.execution.CommonProgramRunConfigurationParameters
import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder

abstract class SpekCommonProgramRunConfigurationParameters(private val _project: Project): CommonProgramRunConfigurationParameters {
    enum class GeneratedNameHint {
        PACKAGE,
        CLASS,
        SCOPE
    }

    var path: Path = PathBuilder.ROOT
    var producerType: ProducerType? = null
    var generatedNameHint: GeneratedNameHint? = null
    private var workingDirectory: String? = null
    private var envs = mutableMapOf<String, String>()
    private var passParentEnvs: Boolean = true
    private var programParameters: String? = null

    override fun getWorkingDirectory(): String? {
        return workingDirectory
    }

    override fun getEnvs(): MutableMap<String, String> {
        return envs
    }

    override fun setWorkingDirectory(workingDirectory: String?) {
        this.workingDirectory = workingDirectory
    }

    override fun setEnvs(envs: MutableMap<String, String>) {
        this.envs = envs
    }

    override fun isPassParentEnvs(): Boolean {
        return passParentEnvs
    }

    override fun setPassParentEnvs(passParentEnvs: Boolean) {
        this.passParentEnvs = passParentEnvs
    }

    override fun setProgramParameters(programParameteres: String?) {
        this.programParameters = programParameteres
    }

    override fun getProgramParameters(): String? {
        return programParameters
    }

    override fun getProject(): Project {
        return _project
    }

    open fun writeExternal(element: Element) {
        JDOMExternalizerUtil.writeField(element, PASS_PARENT_ENVS, passParentEnvs.toString())
        JDOMExternalizerUtil.writeField(element, WORKING_DIRECTORY, workingDirectory)
        JDOMExternalizerUtil.writeField(element, PROGRAM_PARAMETERS, programParameters)
        JDOMExternalizerUtil.writeField(element, PATH, path.serialize())
        EnvironmentVariablesComponent.writeExternal(element, envs)

        if (generatedNameHint != null) {
            JDOMExternalizerUtil.writeField(element, GENERATED_NAME_HINT, generatedNameHint!!.name)
        }
    }

    open fun readExternal(element: Element) {
        passParentEnvs = JDOMExternalizerUtil.readField(element, PASS_PARENT_ENVS, "false").toBoolean()
        workingDirectory = JDOMExternalizerUtil.readField(element, WORKING_DIRECTORY)
        programParameters = JDOMExternalizerUtil.readField(element, PROGRAM_PARAMETERS)
        path = PathBuilder.parse(JDOMExternalizerUtil.readField(element, PATH, ""))
            .build()
        EnvironmentVariablesComponent.readExternal(element, envs)
        generatedNameHint = JDOMExternalizerUtil.readField(element, GENERATED_NAME_HINT)?.let(GeneratedNameHint::valueOf)
    }

    companion object {
        private const val PASS_PARENT_ENVS = "passParentEnvs"
        private const val WORKING_DIRECTORY = "workingDirectory"
        private const val PROGRAM_PARAMETERS = "programParameters"
        private const val GENERATED_NAME_HINT = "generatedNameHint"
        private const val PATH= "path"
    }
}