package org.spekframework.intellij

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.psi.KtClassOrObject

class SpekImplicitUsageProvider: ImplicitUsageProvider {
    override fun isImplicitWrite(element: PsiElement) = false

    override fun isImplicitRead(element: PsiElement) = false

    override fun isImplicitUsage(element: PsiElement): Boolean {
        if (element is KtClassOrObject) {
            return isSpekSubclass(element) && isSpekRunnable(element)
        } else if (element is KtLightClass) {
            val origin = element.kotlinOrigin

            if (origin != null) {
                return isSpekSubclass(origin) && isSpekRunnable(origin)
            }
        }
        return false
    }
}

