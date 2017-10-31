package org.spekframework.spek2.samples

import org.spekframework.spek2.Spek
import kotlin.test.assertEquals

class MemoizedSpec : Spek({
    
    class Screen {
        var displayedText = ""
    }
    
    describe("screen") {

        // Memoized is lazily recreated for every test case (it).
        // That allows you isolate test state from other tests and keep code clean.
        val screen by memoized { Screen() }
        
        it("displays empty text") {
            assertEquals("", screen.displayedText)
        }
        
        context("output digit") {

            beforeEachTest { 
                screen.displayedText = "8"
            }
            
            it("displays digit") {
                assertEquals("8", screen.displayedText)
            }
        }

        // This test will pass even though test above it clearly changed displayed text.
        it("displays 0-length text") {
            assertEquals(0, screen.displayedText.length)
        }
    }
    
})
