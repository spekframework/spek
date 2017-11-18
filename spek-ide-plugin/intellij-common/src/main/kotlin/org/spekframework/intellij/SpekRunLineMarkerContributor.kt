package org.spekframework.intellij

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.util.Function

class SpekRunLineMarkerContributor: RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        val path = extractPath(element)
        return path?.let {
            Info(
                AllIcons.RunConfigurations.TestState.Run,
                TOOLTIP_PROVIDER,
                *ExecutorAction.getActions(0)
            )
        }
    }

    companion object {
        private val TOOLTIP_PROVIDER = Function<PsiElement, String> { "Run path" }
    }
}
