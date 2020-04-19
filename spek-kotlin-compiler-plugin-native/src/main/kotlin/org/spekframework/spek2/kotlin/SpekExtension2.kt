package org.spekframework.spek2.kotlin

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.asJava.toLightClass
import org.jetbrains.kotlin.backend.common.phaser.dumpIrElement
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.extensions.CollectAdditionalSourcesExtension
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.synthetics.findClassDescriptor
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.BindingContextUtils

class SpekExtension2 : CollectAdditionalSourcesExtension {
  override fun collectAdditionalSourcesAndUpdateConfiguration(knownSources: Collection<KtFile>, configuration: CompilerConfiguration, project: Project): Collection<KtFile> {
    knownSources.forEach { file ->
//      val spekClasses = file.findChildrenByClass(KtClassOrObject::class.java)
//        .filter(::isSpekSubclass)
//
//      println("############# $spekClasses")
      file.findChildrenByClass(KtClassOrObject::class.java).forEach { cls ->
        println(cls.toLightClass())
      }

      GlobalSearchScope.projectScope(project)
    }
    return emptyList()
  }

  private fun isSpekSubclass(element: KtClassOrObject): Boolean {
    println("############# ${element.fqName}")
    val superClass = getSuperClass(element)
    val fqName = superClass?.fqName
    if (fqName != null) {
      if (SPEK_CLASSES.contains(fqName.toString())) {
        return true
      } else {
        return isSpekSubclass(superClass)
      }
    }
    return false
  }

  private fun getSuperClass(element: KtClassOrObject): KtClassOrObject? {
    val superTypes = element.superTypeListEntries
    var superClass: KtClassOrObject? = null

    for (entry in superTypes) {
      if (entry is KtSuperTypeCallEntry) {
        println(entry.calleeExpression?.constructorReferenceExpression?.getReferencedNameElement()?.reference)
        val ref = entry.calleeExpression.constructorReferenceExpression
          ?.reference
          ?.resolve()
        superClass = when (ref) {
          is KtPrimaryConstructor -> ref.getContainingClassOrObject()
          is KtClass -> ref
          else -> null
        }

        if (superClass != null) {
          break
        }
      }
    }

    return superClass
  }

  companion object {
    private val SPEK_CLASSES = listOf(
      "org.spekframework.spek2.Spek"
    )
  }
}