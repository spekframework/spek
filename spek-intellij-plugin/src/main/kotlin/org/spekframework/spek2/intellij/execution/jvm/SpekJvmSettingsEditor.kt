package org.spekframework.spek2.intellij.execution.jvm

import com.intellij.application.options.ModulesComboBox
import com.intellij.execution.ui.CommonJavaParametersPanel
import com.intellij.execution.ui.ConfigurationModuleSelector
import com.intellij.execution.ui.DefaultJreSelector
import com.intellij.execution.ui.JrePathEditor
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.ui.TextFieldWithHistory
import org.spekframework.spek2.runtime.scope.PathBuilder
import javax.swing.JPanel
import kotlin.properties.Delegates

/**
 * @author Ranie Jade Ramiso
 */
class SpekJvmSettingsEditor(project: Project): SettingsEditor<SpekJvmRunConfiguration>() {
    private lateinit var panel: JPanel
    private lateinit var mainPanel: JPanel

    private lateinit var commonJavaParameters: CommonJavaParametersPanel
    private lateinit var module: LabeledComponent<ModulesComboBox>
    private lateinit var jrePathEditor: JrePathEditor
    private lateinit var path: LabeledComponent<TextFieldWithHistory>

    var moduleSelector: ConfigurationModuleSelector

    private var selectedModule: Module?
        get() {
            return module.component.selectedModule
        }
        set(value) {
            module.component.selectedModule = value
        }

    private var selectedPath by Delegates.observable(PathBuilder.ROOT) { _, _, value ->
        path.component.setTextAndAddToHistory(value.toString())
    }

    init {
        module.component.fillModules(project)
        moduleSelector = ConfigurationModuleSelector(project, module.component)
        jrePathEditor.setDefaultJreSelector(DefaultJreSelector.fromModuleDependencies(module.component, false))
        commonJavaParameters.setModuleContext(selectedModule)
        commonJavaParameters.setHasModuleMacro()
        module.component.addActionListener {
            commonJavaParameters.setModuleContext(selectedModule)
        }
    }


    override fun resetEditorFrom(configuration: SpekJvmRunConfiguration) {
        selectedModule = configuration.configurationModule.module
        moduleSelector.reset(configuration)
        commonJavaParameters.reset(configuration)
        selectedPath = configuration.data.path
        jrePathEditor.setPathOrName(configuration.alternativeJrePath, configuration.isAlternativeJrePathEnabled)
    }

    override fun applyEditorTo(configuration: SpekJvmRunConfiguration) {
        configuration.alternativeJrePath = jrePathEditor.jrePathOrName
        configuration.isAlternativeJrePathEnabled = jrePathEditor.isAlternativeJreSelected
        configuration.setModule(selectedModule)
        moduleSelector.applyTo(configuration)
        commonJavaParameters.applyTo(configuration)
        configuration.data.path = selectedPath
    }

    override fun createEditor() = panel

    private fun createUIComponents() {
        path = LabeledComponent.create(
            TextFieldWithHistory(),
            "Scope", "West"
        )

        path.component.isEditable = false
    }
}
