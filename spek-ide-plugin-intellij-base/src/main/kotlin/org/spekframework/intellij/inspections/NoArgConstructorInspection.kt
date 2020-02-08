package org.spekframework.intellij.inspections

import com.intellij.codeInspection.*
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.refactoring.pullUp.makeAbstract
import org.jetbrains.kotlin.idea.util.addAnnotation
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getSuperNames

class NoArgConstructorInspection : AbstractKotlinInspection() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : KtTreeVisitorVoid() {
            override fun visitClass(klass: KtClass) {
                val isSpek = klass.getSuperNames().any { it == "Spek" }
                if (!isSpek) return
                val ignored = klass.annotationEntries.any { it.shortName?.toString() == "Ignore" }
                if (ignored) return
                val modifiers = klass.modifierList
                val isAbstract = modifiers != null && modifiers.hasModifier(ABSTRACT)
                if (isAbstract) return
                val hasCreateWith = klass.annotationEntries.any { it.shortName?.toString() == "CreateWith" }
                if (hasCreateWith) return
                val hasNoArgConstructor = klass.allConstructors.any { constr ->
                    constr.getValueParameters().all { param -> param.hasDefaultValue() }
                }
                if (hasNoArgConstructor) return
                val name = klass.nameIdentifier ?: return
                holder.registerProblem(name, "Class #ref needs no-arg constructor #loc", IgnoreTest, MakeAbstract)
            }
        }

    private object MakeAbstract : LocalQuickFix {
        override fun getFamilyName(): String = "Make test class abstract"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val cls = descriptor.psiElement.parent as? KtClass ?: return
            cls.makeAbstract()
        }
    }

    private object IgnoreTest : LocalQuickFix {
        override fun getFamilyName(): String = "Ignore this test"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val cls = descriptor.psiElement.parent as? KtClass ?: return
            cls.addAnnotation(FqName(IGNORE))
        }
    }

    companion object {
        private const val IGNORE = "org.spekframework.spek2.meta.Ignore"
        private val ABSTRACT = KtModifierKeywordToken.softKeywordModifier("abstract")
    }
}