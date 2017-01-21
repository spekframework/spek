package org.jetbrains.spek.table

import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.dsl.TestContainer
import org.jetbrains.spek.meta.Experimental

data class Example1<out P1> internal constructor(val p1: P1)

@Experimental
fun <P1> example(p1: P1): Example1<P1> = Example1(p1)

@Experimental
fun <P1> Spec.with(vararg examples: Example1<P1>, testContent: TestContainer. (P1) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1)
    }
}


data class Example2<out P1, out P2> internal constructor(val p1: P1, val p2: P2)

@Experimental
fun <P1, P2> example(p1: P1, p2: P2): Example2<P1, P2> = Example2(p1, p2)

@Experimental
fun <P1, P2> Spec.with(vararg examples: Example2<P1, P2>, testContent: TestContainer. (P1, P2) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2)
    }
}


data class Example3<out P1, out P2, out P3> internal constructor(val p1: P1, val p2: P2, val p3: P3)

@Experimental
fun <P1, P2, P3> example(p1: P1, p2: P2, p3: P3): Example3<P1, P2, P3> = Example3(p1, p2, p3)

@Experimental
fun <P1, P2, P3> Spec.with(vararg examples: Example3<P1, P2, P3>, testContent: TestContainer. (P1, P2, P3) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3)
    }
}


data class Example4<out P1, out P2, out P3, out P4> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4)

@Experimental
fun <P1, P2, P3, P4> example(p1: P1, p2: P2, p3: P3, p4: P4): Example4<P1, P2, P3, P4> = Example4(p1, p2, p3, p4)

@Experimental
fun <P1, P2, P3, P4> Spec.with(vararg examples: Example4<P1, P2, P3, P4>, testContent: TestContainer. (P1, P2, P3, P4) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4)
    }
}


data class Example5<out P1, out P2, out P3, out P4, out P5> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5)

@Experimental
fun <P1, P2, P3, P4, P5> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): Example5<P1, P2, P3, P4, P5> = Example5(p1, p2, p3, p4, p5)

@Experimental
fun <P1, P2, P3, P4, P5> Spec.with(vararg examples: Example5<P1, P2, P3, P4, P5>, testContent: TestContainer. (P1, P2, P3, P4, P5) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5)
    }
}


data class Example6<out P1, out P2, out P3, out P4, out P5, out P6> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6)

@Experimental
fun <P1, P2, P3, P4, P5, P6> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): Example6<P1, P2, P3, P4, P5, P6> = Example6(p1, p2, p3, p4, p5, p6)

@Experimental
fun <P1, P2, P3, P4, P5, P6> Spec.with(vararg examples: Example6<P1, P2, P3, P4, P5, P6>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6)
    }
}


data class Example7<out P1, out P2, out P3, out P4, out P5, out P6, out P7> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): Example7<P1, P2, P3, P4, P5, P6, P7> = Example7(p1, p2, p3, p4, p5, p6, p7)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7> Spec.with(vararg examples: Example7<P1, P2, P3, P4, P5, P6, P7>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7)
    }
}


data class Example8<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): Example8<P1, P2, P3, P4, P5, P6, P7, P8> = Example8(p1, p2, p3, p4, p5, p6, p7, p8)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8> Spec.with(vararg examples: Example8<P1, P2, P3, P4, P5, P6, P7, P8>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8)
    }
}


data class Example9<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): Example9<P1, P2, P3, P4, P5, P6, P7, P8, P9> = Example9(p1, p2, p3, p4, p5, p6, p7, p8, p9)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9> Spec.with(vararg examples: Example9<P1, P2, P3, P4, P5, P6, P7, P8, P9>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9)
    }
}


data class Example10<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10): Example10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> = Example10(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> Spec.with(vararg examples: Example10<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10)
    }
}


data class Example11<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11): Example11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> = Example11(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> Spec.with(vararg examples: Example11<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11)
    }
}


data class Example12<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12): Example12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> = Example12(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> Spec.with(vararg examples: Example12<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12)
    }
}


data class Example13<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13): Example13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> = Example13(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> Spec.with(vararg examples: Example13<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13)
    }
}


data class Example14<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14): Example14<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14> = Example14(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14> Spec.with(vararg examples: Example14<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14)
    }
}


data class Example15<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15): Example15<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15> = Example15(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15> Spec.with(vararg examples: Example15<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15)
    }
}


data class Example16<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16): Example16<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16> = Example16(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16> Spec.with(vararg examples: Example16<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16)
    }
}


data class Example17<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17): Example17<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17> = Example17(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17> Spec.with(vararg examples: Example17<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17)
    }
}


data class Example18<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17, out P18> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17, val p18: P18)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18): Example18<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18> = Example18(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18> Spec.with(vararg examples: Example18<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17, it.p18)
    }
}


data class Example19<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17, out P18, out P19> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17, val p18: P18, val p19: P19)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19): Example19<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19> = Example19(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19> Spec.with(vararg examples: Example19<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17, it.p18, it.p19)
    }
}


data class Example20<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17, out P18, out P19, out P20> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17, val p18: P18, val p19: P19, val p20: P20)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19, p20: P20): Example20<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20> = Example20(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20> Spec.with(vararg examples: Example20<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17, it.p18, it.p19, it.p20)
    }
}


data class Example21<out P1, out P2, out P3, out P4, out P5, out P6, out P7, out P8, out P9, out P10, out P11, out P12, out P13, out P14, out P15, out P16, out P17, out P18, out P19, out P20, out P21> internal constructor(val p1: P1, val p2: P2, val p3: P3, val p4: P4, val p5: P5, val p6: P6, val p7: P7, val p8: P8, val p9: P9, val p10: P10, val p11: P11, val p12: P12, val p13: P13, val p14: P14, val p15: P15, val p16: P16, val p17: P17, val p18: P18, val p19: P19, val p20: P20, val p21: P21)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21> example(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19, p20: P20, p21: P21): Example21<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21> = Example21(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21)

@Experimental
fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21> Spec.with(vararg examples: Example21<P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21>, testContent: TestContainer. (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21) -> Unit) {
    examples.forEach {
        val proxyContainer = object : TestContainer by this@with {
            override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
                this@with.test("$description ($it)",pending,body)
            }
        }
        testContent.invoke(proxyContainer, it.p1, it.p2, it.p3, it.p4, it.p5, it.p6, it.p7, it.p8, it.p9, it.p10, it.p11, it.p12, it.p13, it.p14, it.p15, it.p16, it.p17, it.p18, it.p19, it.p20, it.p21)
    }
}