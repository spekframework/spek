package org.spekframework.intellij

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.util.Function
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNameReferenceExpression

class SpekRunLineMarkerContributor: RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        val path = when {
            // we only highlight identifiers to avoid conflicting line markers
            isIdentifier(element) -> {
                val parent = element.parent
                when(parent) {
                    // most probably parent is a KtCallExpression
                    is KtNameReferenceExpression -> extractPath(parent.parent)
                    is KtClassOrObject -> extractPath(parent)
                    else -> null
                }
            }
            else -> null
        }

        return path?.let {
            Info(
                AllIcons.RunConfigurations.TestState.Run,
                Function<PsiElement, String> { "Run ${path.name}"},
                *ExecutorAction.getActions(0)
            )
        }
    }
}
