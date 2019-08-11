package org.spekframework.spek2.kotlin

import org.jetbrains.kotlin.backend.common.BackendContext
import org.jetbrains.kotlin.backend.common.CommonBackendContext
import org.jetbrains.kotlin.backend.common.descriptors.WrappedFieldDescriptor
import org.jetbrains.kotlin.backend.common.descriptors.WrappedSimpleFunctionDescriptor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.ir.addChild
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.reportWarning
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptorImpl
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.impl.IrFieldImpl
import org.jetbrains.kotlin.ir.declarations.impl.IrFunctionImpl
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrClassReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrExpressionBodyImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrFieldSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classifierOrFail
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

class SpekExtension : IrGenerationExtension {
    override fun generate(file: IrFile, backendContext: BackendContext, bindingContext: BindingContext) {
        val spekCollector = SpekCollector(file, backendContext)
        file.acceptChildrenVoid(spekCollector)
        spekCollector.generateRegistrations()
    }
}

private class SpekCollector(
        private val file: IrFile,
        private val backendContext: BackendContext
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

    // All of this is trying to create a call that looks like this:
    // registerSpek(SpecObject::class, { SpecObject })
    private fun generateRegistration(declaration: IrClass) {
        // TODO: is this the correct way to find the package? This works, but it feels wrong.
        val launcherPackage = backendContext.builtIns.builtInsModule.getPackage(FqName.fromSegments(listOf("org", "spekframework", "spek2", "launcher")))
        val registrationFunction = launcherPackage.memberScope.getContributedFunctions(Name.identifier("registerSpek"), NoLookupLocation.FROM_BACKEND)
                .single()

        val registrationFunctionSymbol = backendContext.ir.symbols
                .externalSymbolTable.referenceSimpleFunction(registrationFunction)

        val classSymbol = declaration.symbol

        commonContext.createIrBuilder(file.symbol, file.startOffset, file.endOffset).run {
            val call = irCall(registrationFunctionSymbol).apply {
                // TODO: should both of the IrType parameters below be declaration.defaultType?
                // Should one be the equivalent of KClass<Spek> or KClass<DerivedSpek>?
                val classReference = IrClassReferenceImpl(startOffset, endOffset, declaration.defaultType, classSymbol, declaration.defaultType)
                putValueArgument(0, classReference)

                val factoryType = registrationFunctionSymbol.owner.valueParameters[1].type
                val factoryBlock = createFactoryLambdaBlock(declaration, factoryType)

                putValueArgument(1, factoryBlock)
            }

            file.addTopLevelInitializer(call, backendContext)
        }
    }

    private fun createFactoryLambdaBlock(declaration: IrClass, factoryType: IrType): IrBlock {
        return IrBlockImpl(
                declaration.startOffset,
                declaration.endOffset,
                factoryType,
                IrStatementOrigin.LAMBDA
        ).apply {
            val factory = createFactoryLambda(declaration)

            val factoryReference = IrFunctionReferenceImpl(
                    factory.startOffset,
                    factory.endOffset,
                    factoryType,
                    factory.symbol,
                    factory.descriptor,
                    0,
                    0,
                    IrStatementOrigin.LAMBDA
            )

            statements.add(factory)
            statements.add(factoryReference)
        }
    }

    private fun createFactoryLambda(declaration: IrClass): IrFunction = WrappedSimpleFunctionDescriptor().let { descriptor ->
        IrFunctionImpl(
                declaration.startOffset,
                declaration.endOffset,
                IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA,
                IrSimpleFunctionSymbolImpl(descriptor),
                Name.special("<anonymous>"),
                Visibilities.LOCAL,
                Modality.FINAL,
                declaration.defaultType,
                isInline = false,
                isExternal = false,
                isTailrec = false,
                isSuspend = false
        ).apply {
            descriptor.bind(this)
            parent = file

            body = backendContext.createIrBuilder(symbol, symbol.owner.startOffset, symbol.owner.endOffset).irBlockBody {
                +irReturn(irGetObject(declaration.symbol))
            }
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
    val descriptor = WrappedFieldDescriptor(annotations)

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

