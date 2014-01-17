package org.spek.console.impl

import org.junit.Test as test
import org.junit.runner.notification.RunNotifier
import org.mockito.Mockito
import org.spek.impl.events.Multicaster
import org.spek.console.api.ConsoleSpek
import org.spek.impl.Runner
import org.spek.console.reflect.BufferedOutputDevice
import org.spek.console.listeners.text.PlainTextListener
import kotlin.test.assertEquals
import org.spek.api.annotations.skip
import org.spek.reflect.SpecificationRunner
import org.spek.impl.TestFixtureAction

public class SkipTest {
    val notifier = Mockito.mock(javaClass<RunNotifier>())
    val listeners = Multicaster()
    val buffer = StringBuilder()

    fun runTest(case: TestFixtureAction, expected: String) {
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))
        Runner.executeSpek(case, listeners)
        assertEquals(expected.trim(), buffer.toString().trim())
    }

    test fun skipIt() {
        runTest(object: ConsoleSpek() {{
            given("a situation") {
                on("an event") {
                    it("should A") {
                        skip("not ready yet")
                    }

                    it("should B") { }
                }
            }
        }
        }, """
Given given a situation
  On an event
    It should A
    Skipped It 'should A'. Reason: not ready yet
    It should B
""")
    }

    test fun skipOn() {

        runTest(object: ConsoleSpek() {{
            given("a situation") {
                on("an event") {
                    skip("not ready yet")

                    it("should A") { }
                }

                on("another event") {

                    it("should B") { }
                }
            }
        }
        }, """
Given given a situation
  On an event
  Skipped On 'an event'. Reason: not ready yet
  On another event
    It should B
""")
    }


    test fun skipGiven() {
        runTest(object: ConsoleSpek() {{
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
        }
        }, """
Given given a situation
Skipped Given 'given a situation'. Reason: for some reason
""")
    }

    test fun pendingIt() {
        runTest(object: ConsoleSpek() {{
            given("a situation") {
                on("an event") {
                    it("should A") {
                        pending("not implemented yet")
                    }

                    //default pending.
                    it("should B")
                }
            }
        }
        }, """
Given given a situation
  On an event
    It should A
    Pending It 'should A'. Reason: not implemented yet
    It should B
    Pending It 'should B'. Reason: not given
""")
    }

    test fun pendingOn() {
        runTest(object : ConsoleSpek() {{
            given("a situation") {
                //default pending.
                on("an event")

                on("another event") {
                    pending("not implemented yet")
                }
            }
        }
        }, """
Given given a situation
  On an event
  Pending On 'an event'. Reason: not given
  On another event
  Pending On 'another event'. Reason: not implemented yet
""")
    }


    test fun pendingGiven() {
        runTest(object : ConsoleSpek() {{
            given("a situation") {
                pending("for some reason")
            }

            //default pending.
            given("another situation")
        }
        }
                , """
Given given a situation
Pending Given 'given a situation'. Reason: for some reason

Given given another situation
Pending Given 'given another situation'. Reason: not given
""")
    }
}



