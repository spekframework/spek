package org.jetbrains.spek.table

import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.api.dsl.TestContainer

class TestCase1<out P1> internal constructor(val p1: P1)

fun <P1> testCase(p1: P1): TestCase1<P1> = TestCase1 (p1)
fun <P1> Spec.unroll(vararg testCases: TestCase1<P1>, testContent: TestContainer.(P1) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1) }
}


class TestCase2<out P1, out P2> internal constructor(val p1: P1, val p2: P2)

fun <P1, P2> testCase(p1: P1, p2: P2): TestCase2<P1, P2> = TestCase2 (p1, p2)
fun <P1, P2> Spec.unroll(vararg testCases: TestCase2<P1, P2>, testContent: TestContainer.(P1, P2) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2) }
}


class TestCase3<out P1, out P2, out P3> internal constructor(val p1: P1, val p2: P2, val p3: P3)

fun <P1, P2, P3> testCase(p1: P1, p2: P2, p3: P3): TestCase3<P1, P2, P3> = TestCase3 (p1, p2, p3)
fun <P1, P2, P3> Spec.unroll(vararg testCases: TestCase3<P1, P2, P3>, testContent: TestContainer.(P1, P2, P3) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3) }
}


class TestCase4<out P1, out P2, out P3, out P4> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4)

fun <P1, P2, P3, P4> testCase(p1: P1, p2: P2, p3: P3, p4: P4): TestCase4<P1, P2, P3, P4> = TestCase4 (p1, p2, p3, p4)
fun <P1, P2, P3, P4> Spec.unroll(vararg testCases: TestCase4<P1, P2, P3, P4>, testContent: TestContainer.(P1, P2, P3, P4) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4) }
}


class TestCase5<out P1, out P2, out P3, out P4, out P5> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5)

fun <P1, P2, P3, P4, P5> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): TestCase5<P1, P2, P3, P4, P5> = TestCase5 (p1, p2, p3, p4, p5)
fun <P1, P2, P3, P4, P5> Spec.unroll(vararg testCases: TestCase5<P1, P2, P3, P4, P5>, testContent: TestContainer.(P1, P2, P3, P4, P5) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5) }
}


class TestCase6<out P1, out P2, out P3, out P4, out P5, out P6> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6)

fun <P1, P2, P3, P4, P5, P6> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): TestCase6<P1, P2, P3, P4, P5, P6> = TestCase6 (p1, p2, p3, p4, p5, p6)
fun <P1, P2, P3, P4, P5, P6> Spec.unroll(vararg testCases: TestCase6<P1, P2, P3, P4, P5, P6>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6) }
}


class TestCase7<out P1, out P2, out P3, out P4, out P5, out P6, out P7> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7)

fun <P1, P2, P3, P4, P5, P6, P7> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): TestCase7<P1, P2, P3, P4, P5, P6, P7> = TestCase7 (p1, p2, p3, p4, p5, p6, p7)
fun <P1, P2, P3, P4, P5, P6, P7> Spec.unroll(vararg testCases: TestCase7<P1, P2, P3, P4, P5, P6, P7>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7) }
}


class TestCase8<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8)

fun <P1, P2, P3, P4, P5, P6, P7, P8> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): TestCase8<P1, P2, P3, P4, P5, P6, P7, P8> = TestCase8 (p1, p2, p3, p4, p5, p6, p7, p8)
fun <P1, P2, P3, P4, P5, P6, P7, P8> Spec.unroll(vararg testCases: TestCase8<P1, P2, P3, P4, P5, P6, P7, P8>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8) }
}


class TestCase9<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): TestCase9<P1, P2, P3, P4, P5, P6, P7, P8, P9> = TestCase9 (p1, p2, p3, p4, p5, p6, p7, p8, p9)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9> Spec.unroll(vararg testCases: TestCase9<P1, P2, P3, P4, P5, P6, P7, P8, P9>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9) }
}


class TestCase10<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10): TestCase10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> = TestCase10 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> Spec.unroll(vararg testCases: TestCase10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10) }
}


class TestCase11<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11): TestCase11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> = TestCase11 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> Spec.unroll(vararg testCases: TestCase11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11) }
}


class TestCase12<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12): TestCase12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> = TestCase12 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> Spec.unroll(vararg testCases: TestCase12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12) }
}


class TestCase13<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13): TestCase13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> = TestCase13 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> Spec.unroll(vararg testCases: TestCase13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13) }
}


class TestCase14<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14): TestCase14<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14> = TestCase14 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14> Spec.unroll(vararg testCases: TestCase14<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14) }
}


class TestCase15<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15): TestCase15<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15> = TestCase15 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15> Spec.unroll(vararg testCases: TestCase15<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15) }
}


class TestCase16<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16): TestCase16<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16> = TestCase16 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16> Spec.unroll(vararg testCases: TestCase16<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16) }
}


class TestCase17<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17): TestCase17<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17> = TestCase17 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17> Spec.unroll(vararg testCases: TestCase17<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17) }
}


class TestCase18<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17, out P18> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17, val p18: P18)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18): TestCase18<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18> = TestCase18 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18> Spec.unroll(vararg testCases: TestCase18<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17, it.p18) }
}


class TestCase19<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17, out P18, out P19> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17, val p18: P18, val p19: P19)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19): TestCase19<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19> = TestCase19 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19> Spec.unroll(vararg testCases: TestCase19<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17, it.p18, it.p19) }
}


class TestCase20<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17, out P18, out P19, out P20> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17, val p18: P18, val p19: P19, val p20: P20)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19, p20: P20): TestCase20<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20> = TestCase20 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20> Spec.unroll(vararg testCases: TestCase20<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17, it.p18, it.p19, it.p20) }
}


class TestCase21<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17, out P18, out P19, out P20, out P21> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17, val p18: P18, val p19: P19, val p20: P20, val p21: P21)

fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21> testCase(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19, p20: P20, p21: P21): TestCase21<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21> = TestCase21 (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21)
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21> Spec.unroll(vararg testCases: TestCase21<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21>, testContent: TestContainer.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21) -> Unit) {
    testCases.forEach { testContent.invoke(this, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17, it.p18, it.p19, it.p20, it.p21) }
}

