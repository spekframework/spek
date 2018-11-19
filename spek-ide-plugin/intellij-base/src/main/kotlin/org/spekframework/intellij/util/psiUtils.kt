package org.spekframework.intellij.util

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject

fun maybeGetContext(element: PsiElement): PsiElement? {
    return when (element) {
        is KtClassOrObject -> element
        is KtCallExpression -> element
        else -> PsiTreeUtil.getContextOfType(element, false, KtClassOrObject::class.java, KtCallExpression::class.java)
    }
}