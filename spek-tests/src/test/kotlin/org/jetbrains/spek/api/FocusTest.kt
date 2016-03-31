package org.jetbrains.spek.api

import org.junit.Test as test

class FocusTest : IntegrationTestCase() {
    @test fun focusIt() =
            runTest(data {
                describe("a situation") {
                    it("should A") { }

                    fit("should B") { }

                    it("should C") { }

                    fit("should D") { }
                }
            }, """Spek: START
                a situation: START
                it should B: START
                it should B: FINISH
                a situation: FINISH
                Spek: FINISH

                Spek: START
                a situation: START
                it should D: START
                it should D: FINISH
                a situation: FINISH
                Spek: FINISH""")


    @test fun focusDescribe() =
            runTest(data {
                describe("a situation") {
                    describe("an event") {
                        it("should A") { }
                    }

                    fdescribe("another event") {
                        it("should B") { }
                        it("should C") { }
                    }

                    describe("a third event") {
                        it("should D") { }
                    }
                }
            }, """Spek: START
                a situation: START
                another event: START
                it should B: START
                it should B: FINISH
                another event: FINISH
                a situation: FINISH
                Spek: FINISH

                Spek: START
                a situation: START
                another event: START
                it should C: START
                it should C: FINISH
                another event: FINISH
                a situation: FINISH
                Spek: FINISH""")


    @test fun nestedItFocus() =
            runTest(data {
                describe("a situation") {
                    describe("an event") {
                        it("should A") { }
                    }

                    describe("another event") {
                        it("should B") { }
                        it("should C") { }
                    }

                    describe("a third event") {
                        fit("should D") { }
                    }
                }
            }, """Spek: START
                a situation: START
                a third event: START
                it should D: START
                it should D: FINISH
                a third event: FINISH
                a situation: FINISH
                Spek: FINISH""")

}