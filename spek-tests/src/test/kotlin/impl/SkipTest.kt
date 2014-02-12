package org.spek.test

import org.junit.Test as test
import org.spek.test.IntegrationTestCase.Data
import kotlin.test.*
import org.junit.*
import org.spek.*

public class SkipTest : IntegrationTestCase() {
    test fun skipIt() =
            runTest(data {
                given("a situation") {
                    on("an event") {
                        it("should A") {
                            skip("not ready yet")
                        }

                        it("should B") { }
                    }
                }
            }, """SPEK: 42 START
                SPEK: 42 GIVEN: given a situation START
                SPEK: 42 GIVEN: given a situation ON: on an event START
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should A START
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should A SKIP:not ready yet
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should A FINISH
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should B START
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should B FINISH
                SPEK: 42 GIVEN: given a situation ON: on an event FINISH
                SPEK: 42 GIVEN: given a situation FINISH
                SPEK: 42 FINISH""")


    test fun skipOn() =
            runTest(data {
                given("a situation") {
                    on("an event") {
                        skip("not ready yet")

                        it("should A") { }
                    }

                    on("another event") {

                        it("should B") { }
                    }
                }
            }, """SPEK: 42 START
                SPEK: 42 GIVEN: given a situation START
                SPEK: 42 GIVEN: given a situation ON: on an event START
                SPEK: 42 GIVEN: given a situation ON: on an event SKIP:not ready yet
                SPEK: 42 GIVEN: given a situation ON: on an event FINISH
                SPEK: 42 GIVEN: given a situation ON: on another event START
                SPEK: 42 GIVEN: given a situation ON: on another event IT: it should B START
                SPEK: 42 GIVEN: given a situation ON: on another event IT: it should B FINISH
                SPEK: 42 GIVEN: given a situation ON: on another event FINISH
                SPEK: 42 GIVEN: given a situation FINISH
                SPEK: 42 FINISH""")


    test fun skipGiven() =
            runTest(data {
                given("a situation") {
                    skip("for some reason")

                    on("an event") {
                        it("should A") { }
                    }

                    on("another event") {
                        it("should B") {
                            pending("ok!!!")
                        }
                    }
                }
            }, """ SPEK: 42 START
                SPEK: 42 GIVEN: given a situation START
                SPEK: 42 GIVEN: given a situation SKIP:for some reason
                SPEK: 42 GIVEN: given a situation FINISH
                SPEK: 42 FINISH""")

    test fun pendingIt() =
            runTest(data {
                given("a situation") {
                    on("an event") {
                        it("should A") {
                            pending("not implemented yet")
                        }

                        //default pending.
                        it("should B")
                    }
                }
            }, """SPEK: 42 START
                SPEK: 42 GIVEN: given a situation START
                SPEK: 42 GIVEN: given a situation ON: on an event START
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should A START
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should A PEND:not implemented yet
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should A FINISH
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should B START
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should B PEND:Not implemented.
                SPEK: 42 GIVEN: given a situation ON: on an event IT: it should B FINISH
                SPEK: 42 GIVEN: given a situation ON: on an event FINISH
                SPEK: 42 GIVEN: given a situation FINISH
                SPEK: 42 FINISH""")


    test fun pendingOn() =
            runTest(data {
                given("a situation") {
                    //default pending.
                    on("an event")

                    on("another event") {
                        pending("not implemented yet")
                    }
                }
            }, """SPEK: 42 START
                SPEK: 42 GIVEN: given a situation START
                SPEK: 42 GIVEN: given a situation ON: on an event START
                SPEK: 42 GIVEN: given a situation ON: on an event PEND:Not implemented.
                SPEK: 42 GIVEN: given a situation ON: on an event FINISH
                SPEK: 42 GIVEN: given a situation ON: on another event START
                SPEK: 42 GIVEN: given a situation ON: on another event PEND:not implemented yet
                SPEK: 42 GIVEN: given a situation ON: on another event FINISH
                SPEK: 42 GIVEN: given a situation FINISH
                SPEK: 42 FINISH""")


    test fun pendingGiven() =
            runTest(data {
                given("a situation") {
                    pending("for some reason")
                }

                //default pending.
                given("another situation")
            },
                    """SPEK: 42 START
            SPEK: 42 GIVEN: given a situation START
            SPEK: 42 GIVEN: given a situation PEND:for some reason
            SPEK: 42 GIVEN: given a situation FINISH
            SPEK: 42 GIVEN: given another situation START
            SPEK: 42 GIVEN: given another situation PEND:Not implemented.
            SPEK: 42 GIVEN: given another situation FINISH
            SPEK: 42 FINISH""")
}