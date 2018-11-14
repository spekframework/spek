package org.spekframework.intellij

import com.intellij.execution.CommonJavaRunConfigurationParameters
import com.intellij.execution.configurations.JavaRunConfigurationModule
import org.spekframework.intellij.support.SpekJvmCommonRunConfigurationParameters

interface SpekBaseJvmRunConfiguration: SpekRunConfiguration<SpekJvmCommonRunConfigurationParameters>, CommonJavaRunConfigurationParameters {
    val configurationModule: JavaRunConfigurationModule

    override fun setAlternativeJrePath(path: String?) {
        data.alternativeJrePath = path
    }

    override fun setVMParameters(vmParameters: String?) {
        data.vmParameters = vmParameters
    }

    override fun isAlternativeJrePathEnabled(): Boolean {
        return data.isAlternativeJrePathEnabled
    }

    override fun getPackage(): String? {
        return data.`package`
    }

    override fun getRunClass(): String? {
        return data.runClass
    }

    override fun getWorkingDirectory(): String? {
        return data.workingDirectory
    }

    override fun setAlternativeJrePathEnabled(enabled: Boolean) {
        data.isAlternativeJrePathEnabled = enabled
    }

    override fun getVMParameters(): String {
        return data.vmParameters ?: ""
    }

    override fun getAlternativeJrePath(): String? {
        return data.alternativeJrePath
    }
}