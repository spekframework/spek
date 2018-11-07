package org.spekframework.intellij

import com.intellij.execution.CommonProgramRunConfigurationParameters
import com.intellij.execution.Location
import com.intellij.execution.PsiLocation
import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.ModuleBasedConfiguration
import com.intellij.execution.configurations.RunConfigurationModule
import com.intellij.execution.testframework.sm.runner.SMTestLocator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.jdom.Element
import org.spekframework.intellij.domain.ScopeDescriptorCache
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.isRoot
import javax.swing.Icon

abstract class SpekBaseConfigurationType(id: String, displayName: String, icon: Icon): ConfigurationTypeBase(
    id,
    displayName,
    "Run Spek 2 tests.",
    icon
)

enum class ConfigurationKey(val key: String) {
    PATH("path"),
    PASS_PARENT_ENVS("passParentEnvs"),
    WORKING_DIRECTORY("workingDirectory"),
    PROGRAM_PARAMETERS("programParameters");
}


abstract class SpekBaseRunConfiguration<T: RunConfigurationModule>(name: String,
                                                                   configurationModule: T,
                                                                   factory: ConfigurationFactory)
    : ModuleBasedConfiguration<T>(name, configurationModule, factory), CommonProgramRunConfigurationParameters {

    private var workingDirectory: String? = null
    private var envs = mutableMapOf<String, String>()
    private var passParentEnvs: Boolean = false
    private var programParameters: String? = null

    var path: Path = PathBuilder.ROOT

    var producerType: ProducerType? = null

    override fun getWorkingDirectory() = workingDirectory

    override fun getEnvs() = envs

    override fun setWorkingDirectory(value: String?) {
        workingDirectory = value
    }

    override fun setEnvs(envs: MutableMap<String, String>) {
        this.envs = envs
    }

    override fun isPassParentEnvs() = passParentEnvs

    override fun setPassParentEnvs(passParentEnvs: Boolean) {
        this.passParentEnvs = passParentEnvs
    }

    override fun setProgramParameters(value: String?) {
        programParameters = value
    }

    override fun getProgramParameters() = programParameters

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, ConfigurationKey.PASS_PARENT_ENVS.key, passParentEnvs.toString())
        JDOMExternalizerUtil.writeField(element, ConfigurationKey.WORKING_DIRECTORY.key, workingDirectory)
        JDOMExternalizerUtil.writeField(element, ConfigurationKey.PATH.key, path.serialize())
        JDOMExternalizerUtil.writeField(element, ConfigurationKey.PROGRAM_PARAMETERS.key, programParameters)
        EnvironmentVariablesComponent.writeExternal(element, envs)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        passParentEnvs = JDOMExternalizerUtil.readField(element, ConfigurationKey.PASS_PARENT_ENVS.key, "false").toBoolean()
        workingDirectory = JDOMExternalizerUtil.readField(element, ConfigurationKey.WORKING_DIRECTORY.key)
        programParameters = JDOMExternalizerUtil.readField(element, ConfigurationKey.PROGRAM_PARAMETERS.key)
        path = PathBuilder.parse(JDOMExternalizerUtil.readField(element, ConfigurationKey.PATH.key, ""))
            .build()
        EnvironmentVariablesComponent.readExternal(element, envs)
    }

    override fun suggestedName(): String {
        val parent = path.parent

        val prefix = producerType?.let {
            "($it)"
        } ?: ""

        return if (path.name.isEmpty() && parent != null && parent.isRoot) {
            "$prefix Spek tests in <default package>"
        } else if (parent != null && parent.isRoot) {
            "$prefix Spek tests ${path.name}"
        } else {
            "$prefix ${path.name}"
        }.trim()
    }
}

object SpekScopeLocator: SMTestLocator {
    override fun getLocation(protocol: String, path: String, project: Project, scope: GlobalSearchScope): MutableList<Location<PsiElement>> {
        if (protocol != "spek") {
            return mutableListOf()
        }
        val locations = mutableListOf<Location<PsiElement>>()
        val path = PathBuilder.parse(path).build()
        val descriptor = ScopeDescriptorCache.findDescriptor(path)

        if (descriptor != null) {
            locations.add(PsiLocation(descriptor.element))
        }
        return locations
    }

}