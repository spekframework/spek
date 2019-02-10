import org.spekframework.spek2.integration.*
import org.spekframework.spek2.launcher.registerSpek
import org.spekframework.spek2.launcher.spekMain

fun main(args: Array<String>) {
    registerSpek(CalculatorSpecs::class, { CalculatorSpecs })
    registerSpek(EmptyGroupTest::class, { EmptyGroupTest })
    registerSpek(CalculatorSpecs::class, { CalculatorSpecs })
    registerSpek(MemoizedTests::class, { MemoizedTests })
    registerSpek(NonUniquePathTest::class, { NonUniquePathTest })
    registerSpek(SetFeature::class, { SetFeature })
    registerSpek(SetSpec::class, { SetSpec })
    registerSpek(SkipTest::class, { SkipTest })

    spekMain(args)
}
