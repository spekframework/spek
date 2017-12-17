package org.spekframework.intellij

import com.intellij.application.options.ModulesComboBox
import com.intellij.execution.ui.CommonJavaParametersPanel
import com.intellij.execution.ui.ConfigurationModuleSelector
import com.intellij.execution.ui.DefaultJreSelector
import com.intellij.execution.ui.JrePathEditor
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.TextFieldWithHistory
import org.spekframework.spek2.runtime.scope.PathBuilder
import javax.swing.JPanel
import kotlin.properties.Delegates

/**
 * @author Ranie Jade Ramiso
 */
class Spek2JvmSettingsEditor(project: Project): SettingsEditor<Spek2JvmRunConfiguration>() {
    lateinit var panel: JPanel
    lateinit var mainPanel: JPanel

    lateinit var commonJavaParameters: CommonJavaParametersPanel
    lateinit var module: LabeledComponent<ModulesComboBox>
    lateinit var jrePathEditor: JrePathEditor
    lateinit var path: LabeledComponent<TextFieldWithHistory>

    var moduleSelector: ConfigurationModuleSelector

    private var selectedModule: Module?
        get() {
            return module.component.selectedModule
        }
        set(value) {
            module.component.selectedModule = value
        }

    private var selectedPath by Delegates.observable(PathBuilder.ROOT) { _, _, value ->
        path.component.setTextAndAddToHistory(value.serialize())
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


    override fun resetEditorFrom(configuration: Spek2JvmRunConfiguration) {
        selectedModule = configuration.configurationModule.module
        moduleSelector.reset(configuration)
        commonJavaParameters.reset(configuration)
        selectedPath = configuration.path
    }

    override fun applyEditorTo(configuration: Spek2JvmRunConfiguration) {
        configuration.setModule(selectedModule)
        moduleSelector.applyTo(configuration)
        commonJavaParameters.applyTo(configuration)
        configuration.path = selectedPath
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
