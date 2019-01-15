package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek

object EmptyGroupTest: Spek({
    group("empty group is valid") {
        // should not cause any failures
    }

    test("there should be at least one test in the tree") {}
})
