package org.jetbrains.spek.table

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.api.lifecycle.LifecycleListener

class SpekSpecs : Spek({
    describe("Spek table driven extension") {
        given("a spec container") {
            val countingTestContainer: CountingTestContainer = CountingTestContainer()
            beforeEachTest {
                countingTestContainer.reset()
            }
            it("accepts table data of length 1 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1)
                    ) { p1: Int ->
                        it("should be visible with ints $p1") {}
                    }
                    unroll(
                            testCase("1")
                    ) { p1: String ->
                        it("should be visible with strings $p1") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 2 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2)
                    ) { p1: Int, p2: Int ->
                        it("should be visible with ints $p1, $p2") {}
                    }
                    unroll(
                            testCase("1", "2")
                    ) { p1: String, p2: String ->
                        it("should be visible with strings $p1, $p2") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 3 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3)
                    ) { p1: Int, p2: Int, p3: Int ->
                        it("should be visible with ints $p1, $p2, $p3") {}
                    }
                    unroll(
                            testCase("1", "2", "3")
                    ) { p1: String, p2: String, p3: String ->
                        it("should be visible with strings $p1, $p2, $p3") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 4 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4")
                    ) { p1: String, p2: String, p3: String, p4: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 5 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 6 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 7 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 8 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 9 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 10 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 11 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 12 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 13 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 14 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 15 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 16 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 17 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 18 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int, p18: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String, p18: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 19 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int, p18: Int, p19: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String, p18: String, p19: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 20 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int, p18: Int, p19: Int, p20: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19, $p20") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String, p18: String, p19: String, p20: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19, $p20") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }


            it("accepts table data of length 21 with arguments of any type") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int, p18: Int, p19: Int, p20: Int, p21: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19, $p20, $p21") {}
                    }
                    unroll(
                            testCase("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String, p18: String, p19: String, p20: String, p21: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19, $p20, $p21") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

        }
    }
})


private class CountingTestContainer : TestContainer, Spec {
    override fun beforeGroup(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterGroup(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun group(description: String, pending: Pending, body: SpecBody.() -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun action(description: String, pending: Pending, body: ActionBody.() -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> memoized(mode: CachingMode, factory: () -> T): LifecycleAware<T> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beforeEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerListener(listener: LifecycleListener) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
        testNumber++
    }

    var testNumber: Int = 0

    fun reset() {
        testNumber = 0
    }


}