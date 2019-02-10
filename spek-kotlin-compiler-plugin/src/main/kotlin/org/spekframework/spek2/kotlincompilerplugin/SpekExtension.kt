package org.spekframework.spek2.kotlincompilerplugin

import org.jetbrains.kotlin.backend.common.BackendContext
import org.jetbrains.kotlin.backend.common.CommonBackendContext
import org.jetbrains.kotlin.backend.common.descriptors.WrappedPropertyDescriptor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.ir.addChild
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.reportWarning
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.SourceElement
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptorImpl
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.findClassAcrossModuleDependencies
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.impl.IrFieldImpl
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrExpressionBodyImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrFieldSymbolImpl
import org.jetbrains.kotlin.ir.types.classifierOrFail
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

class SpekExtension : IrGenerationExtension {
    override fun generate(file: IrFile, backendContext: BackendContext, bindingContext: BindingContext) {
        val spekCollector = SpekCollector(file, backendContext, bindingContext)
        file.acceptChildrenVoid(spekCollector)
        spekCollector.generateRegistrations()
    }
}

private class SpekCollector(
        private val file: IrFile,
        private val backendContext: BackendContext,
        private val bindingContext: BindingContext
) : IrElementVisitorVoid {
    private val spekClassName = "org.spekframework.spek2.Spek"
    private val commonContext: CommonBackendContext = backendContext.ir.context
    private var collectedSpeks = mutableListOf<IrClass>()

    override fun visitElement(element: IrElement) {
        element.acceptChildrenVoid(this)
    }

    override fun visitClass(declaration: IrClass) {
        super.visitClass(declaration)

        if (!declaration.isSpek) {
            return
        }

        if (declaration.kind != ClassKind.OBJECT) {
            if (!declaration.isAbstract) {
                commonContext.reportWarning("Declaration ${declaration.name} inherits from $spekClassName but is not an object (it has kind ${declaration.kind}) and so will be not be run.", file, declaration)
            }

            return
        }

        collectedSpeks.add(declaration)
    }

    fun generateRegistrations() {
        collectedSpeks.forEach { generateRegistration(it) }
    }

    private fun generateRegistration(declaration: IrClass) {
        println("In plugin: generating registration for ${declaration.name}")

        val kotlinIoPackage = backendContext.builtIns.builtInsModule.getPackage(FqName.fromSegments(listOf("kotlin", "io")))
        val function = kotlinIoPackage.memberScope.getContributedFunctions(Name.identifier("println"), NoLookupLocation.FROM_BACKEND)
                .single { it.valueParameters.singleOrNull()?.type == backendContext.builtIns.stringType }

        val functionSymbol = backendContext.ir.symbols
                .externalSymbolTable.referenceSimpleFunction(function)

        commonContext.createIrBuilder(file.symbol, file.startOffset, file.endOffset).run {
            val call = irCall(functionSymbol).apply {
                putValueArgument(0, irString("In application: hello from ${declaration.name}"))
            }

            file.addTopLevelInitializer(call, backendContext)
        }
    }

    private val IrClass.isSpek: Boolean
        get() = superTypes.any { it.classifierOrFail.descriptor.fqNameSafe.asString() == spekClassName }

    private val IrClass.isAbstract: Boolean
        get() = this.modality == Modality.ABSTRACT
}

private var topLevelInitializersCounter = 0

// Taken from org.jetbrains.kotlin.ir.util/IrUtils2.kt
private fun IrFile.addTopLevelInitializer(expression: IrExpression, context: BackendContext) {
    val threadLocalAnnotation = context.builtIns.builtInsModule.findClassAcrossModuleDependencies(
            ClassId.topLevel(FqName("kotlin.native.concurrent.ThreadLocal")))!!

    val annotations = Annotations.create(listOf(
            AnnotationDescriptorImpl(threadLocalAnnotation.defaultType, emptyMap(), SourceElement.NO_SOURCE)
    ))

    // This was a WrappedFieldDescriptor in the original version, but this isn't accessible to us.
    // Using a WrappedPropertyDescriptor doesn't seem to have any impact on anything.
    val descriptor = WrappedPropertyDescriptor(annotations)

    val irField = IrFieldImpl(
            expression.startOffset, expression.endOffset,
            IrDeclarationOrigin.DEFINED,
            IrFieldSymbolImpl(descriptor),
            Name.identifier("\$spekTopLevelInitializer${topLevelInitializersCounter++}"),
            expression.type,
            Visibilities.PRIVATE,
            isFinal = true,
            isExternal = false,
            isStatic = true
    ).apply {
        descriptor.bind(this)

        initializer = IrExpressionBodyImpl(expression.startOffset, expression.endOffset, expression)
    }

    addChild(irField)
}

