package org.spekframework.spek2.intellij.support

import com.intellij.execution.CommonJavaRunConfigurationParameters
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element

class SpekJvmCommonRunConfigurationParameters(project: Project)
    : SpekCommonProgramRunConfigurationParameters(project), CommonJavaRunConfigurationParameters {

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

    companion object {
        private const val VM_PARAMETERS = "vmParameters"
        private const val ALTERNATIVE_JRE_PATH = "alternativeJrePath"
        private const val ALTERNATIVE_JRE_PATH_ENABLED = "alternativeJrePathEnabled"
    }
}