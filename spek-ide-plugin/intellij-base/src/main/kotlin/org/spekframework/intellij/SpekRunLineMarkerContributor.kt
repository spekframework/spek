package org.spekframework.intellij

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.util.Function
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.spekframework.intellij.domain.ScopeDescriptorCache

class SpekRunLineMarkerContributor: RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        val descriptorCache = checkNotNull(
            element.project.getComponent(ScopeDescriptorCache::class.java)
        )
        val path = when (element) {
            is KtClassOrObject -> descriptorCache.fromClassOrObject(element)
            is KtCallExpression -> descriptorCache.fromCallExpression(element)
            else -> null
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
