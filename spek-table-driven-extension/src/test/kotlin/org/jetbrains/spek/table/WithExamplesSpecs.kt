package org.jetbrains.spek.table

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.contains
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

class WithExamplesSpecs : Spek({
    describe("Spek table driven extension") {
        given("a spec container") {
            val countingTestContainer: CountingTestContainer = CountingTestContainer()
            beforeEachTest {
                countingTestContainer.reset()
            }
            it("accepts table data of length 1 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1)
                    ) { p1: Int ->
                        it("should be visible with ints $p1") {}
                    }
                    with(
                        example("1")
                    ) { p1: String ->
                        it("should be visible with strings $p1") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 1 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1")
                    ) { p1 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()))
            }


            it("accepts table data of length 2 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2)
                    ) { p1: Int, p2: Int ->
                        it("should be visible with ints $p1, $p2") {}
                    }
                    with(
                        example("1", "2")
                    ) { p1: String, p2: String ->
                        it("should be visible with strings $p1, $p2") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 2 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2")
                    ) { p1, p2 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()))
            }


            it("accepts table data of length 3 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3)
                    ) { p1: Int, p2: Int, p3: Int ->
                        it("should be visible with ints $p1, $p2, $p3") {}
                    }
                    with(
                        example("1", "2", "3")
                    ) { p1: String, p2: String, p3: String ->
                        it("should be visible with strings $p1, $p2, $p3") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 3 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3")
                    ) { p1, p2, p3 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()))
            }


            it("accepts table data of length 4 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4") {}
                    }
                    with(
                        example("1", "2", "3", "4")
                    ) { p1: String, p2: String, p3: String, p4: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 4 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4")
                    ) { p1, p2, p3, p4 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()))
            }


            it("accepts table data of length 5 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 5 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5")
                    ) { p1, p2, p3, p4, p5 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()))
            }


            it("accepts table data of length 6 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 6 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6")
                    ) { p1, p2, p3, p4, p5, p6 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()))
            }


            it("accepts table data of length 7 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 7 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7")
                    ) { p1, p2, p3, p4, p5, p6, p7 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()))
            }


            it("accepts table data of length 8 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 8 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()))
            }


            it("accepts table data of length 9 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 9 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()))
            }


            it("accepts table data of length 10 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 10 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()))
            }


            it("accepts table data of length 11 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 11 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()))
            }


            it("accepts table data of length 12 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 12 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()))
            }


            it("accepts table data of length 13 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 13 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12", "param13")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()) and contains("param13".toRegex()))
            }


            it("accepts table data of length 14 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 14 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12", "param13", "param14")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()) and contains("param13".toRegex()) and contains("param14".toRegex()))
            }


            it("accepts table data of length 15 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 15 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12", "param13", "param14", "param15")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()) and contains("param13".toRegex()) and contains("param14".toRegex()) and contains("param15".toRegex()))
            }


            it("accepts table data of length 16 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 16 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12", "param13", "param14", "param15", "param16")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()) and contains("param13".toRegex()) and contains("param14".toRegex()) and contains("param15".toRegex()) and contains("param16".toRegex()))
            }


            it("accepts table data of length 17 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 17 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12", "param13", "param14", "param15", "param16", "param17")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()) and contains("param13".toRegex()) and contains("param14".toRegex()) and contains("param15".toRegex()) and contains("param16".toRegex()) and contains("param17".toRegex()))
            }


            it("accepts table data of length 18 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int, p18: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String, p18: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 18 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12", "param13", "param14", "param15", "param16", "param17", "param18")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()) and contains("param13".toRegex()) and contains("param14".toRegex()) and contains("param15".toRegex()) and contains("param16".toRegex()) and contains("param17".toRegex()) and contains("param18".toRegex()))
            }


            it("accepts table data of length 19 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int, p18: Int, p19: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String, p18: String, p19: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 19 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12", "param13", "param14", "param15", "param16", "param17", "param18", "param19")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()) and contains("param13".toRegex()) and contains("param14".toRegex()) and contains("param15".toRegex()) and contains("param16".toRegex()) and contains("param17".toRegex()) and contains("param18".toRegex()) and contains("param19".toRegex()))
            }


            it("accepts table data of length 20 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int, p18: Int, p19: Int, p20: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19, $p20") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String, p18: String, p19: String, p20: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19, $p20") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 20 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12", "param13", "param14", "param15", "param16", "param17", "param18", "param19", "param20")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()) and contains("param13".toRegex()) and contains("param14".toRegex()) and contains("param15".toRegex()) and contains("param16".toRegex()) and contains("param17".toRegex()) and contains("param18".toRegex()) and contains("param19".toRegex()) and contains("param20".toRegex()))
            }


            it("accepts table data of length 21 with arguments of any type") {
                val with: Spek = wrap {
                    with(
                        example(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21)
                    ) { p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int, p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int, p18: Int, p19: Int, p20: Int, p21: Int ->
                        it("should be visible with ints $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19, $p20, $p21") {}
                    }
                    with(
                        example("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21")
                    ) { p1: String, p2: String, p3: String, p4: String, p5: String, p6: String, p7: String, p8: String, p9: String, p10: String, p11: String, p12: String, p13: String, p14: String, p15: String, p16: String, p17: String, p18: String, p19: String, p20: String, p21: String ->
                        it("should be visible with strings $p1, $p2, $p3, $p4, $p5, $p6, $p7, $p8, $p9, $p10, $p11, $p12, $p13, $p14, $p15, $p16, $p17, $p18, $p19, $p20, $p21") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }

            it("adds 21 parameters to the description") {
                val with: Spek = wrap {
                    with(
                        example("param1", "param2", "param3", "param4", "param5", "param6", "param7", "param8", "param9", "param10", "param11", "param12", "param13", "param14", "param15", "param16", "param17", "param18", "param19", "param20", "param21")
                    ) { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21 ->
                        it("should be completed") {}
                    }
                }
                with.spec.invoke(countingTestContainer)
                countingTestContainer.testNames shouldMatch has("test description",
                    { it[0] }, contains("param1".toRegex()) and contains("param2".toRegex()) and contains("param3".toRegex()) and contains("param4".toRegex()) and contains("param5".toRegex()) and contains("param6".toRegex()) and contains("param7".toRegex()) and contains("param8".toRegex()) and contains("param9".toRegex()) and contains("param10".toRegex()) and contains("param11".toRegex()) and contains("param12".toRegex()) and contains("param13".toRegex()) and contains("param14".toRegex()) and contains("param15".toRegex()) and contains("param16".toRegex()) and contains("param17".toRegex()) and contains("param18".toRegex()) and contains("param19".toRegex()) and contains("param20".toRegex()) and contains("param21".toRegex()))
            }

        }
    }
})
