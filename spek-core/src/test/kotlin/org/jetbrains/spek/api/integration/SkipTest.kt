package org.jetbrains.spek.api.integration

import org.junit.Test as test

class SkipTest : IntegrationTestCase() {
    @test fun skipIt() =
            runTest(data {
                given("a situation") {
                    on("an event") {
                        xit("should A") { }

                        it("should B") { }
                    }
                }
            }, """Spek: START
                given a situation: START
                on an event: START
                it should A: IGNORE
                on an event: FINISH
                given a situation: FINISH
                Spek: FINISH

                Spek: START
                given a situation: START
                on an event: START
                it should B: START
                it should B: FINISH
                on an event: FINISH
                given a situation: FINISH
                Spek: FINISH""")


    @test fun skipOn() =
            runTest(data {
                given("a situation") {
                    xon("an event") {
                        it("should A") { }
                    }

                    on("another event") {
                        it("should B") { }
                    }
                }
            }, """Spek: START
                given a situation: START
                on an event: IGNORE
                given a situation: FINISH
                Spek: FINISH

                Spek: START
                given a situation: START
                on another event: START
                it should B: START
                it should B: FINISH
                on another event: FINISH
                given a situation: FINISH
                Spek: FINISH""")


    @test fun skipGiven() =
            runTest(data {
                xgiven("a situation") {

                    on("an event") {
                        it("should A") { }
                    }

                    on("another event") {
                        xit("should B") { }
                    }
                }
            }, """Spek: START
                given a situation: IGNORE
                Spek: FINISH""")

    @test fun pendingIt() =
            runTest(data {
                given("a situation") {
                    on("an event") {
                        xit("should A") { }

                        xit("should B") { }
                    }
                }
            }, """Spek: START
                given a situation: START
                on an event: START
                it should A: IGNORE
                on an event: FINISH
                given a situation: FINISH
                Spek: FINISH

                Spek: START
                given a situation: START
                on an event: START
                it should B: IGNORE
                on an event: FINISH
                given a situation: FINISH
                Spek: FINISH""")


    @test fun pendingOn() =
            runTest(data {
                given("a situation") {
                    //default pending.
                    xon("an event") { }

                    xon("another event") { }
                }
            }, """Spek: START
                given a situation: START
                on an event: IGNORE
                given a situation: FINISH
                Spek: FINISH

                Spek: START
                given a situation: START
                on another event: IGNORE
                given a situation: FINISH
                Spek: FINISH""")


    @test fun pendingGiven() =
            runTest(data {
                xgiven("a situation") { }

                xgiven("another situation") { }
            }, """Spek: START
            given a situation: IGNORE
            Spek: FINISH

            Spek: START
            given another situation: IGNORE
            Spek: FINISH""")
}