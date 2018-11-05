package org.spekframework.intellij

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.Function
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.spekframework.intellij.domain.ScopeDescriptorCache

class SpekRunLineMarkerContributor: RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        if (element !is KtNameReferenceExpression) {
            return null
        }

        val context = PsiTreeUtil.getContextOfType(element,true, KtClassOrObject::class.java, KtCallExpression::class.java)

        val path = if (context != null) {
            if (context is KtClassOrObject) {
                ScopeDescriptorCache.toDescriptor(context)
            } else if (context is KtCallExpression) {
                ScopeDescriptorCache.toDescriptor(context)
            } else {
                null
            }
        } else {
            null
        }?.path

        return path?.let {
            Info(
                AllIcons.RunConfigurations.TestState.Run,
                Function<PsiElement, String> { "[Spek] Run ${path.name}"},
                *ExecutorAction.getActions(0)
            )
        }
    }
}
