package testData.package1

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SpekTestWithFailureDuringGroupDiscoveryPhase : Spek({
    describe("group") {
        throw Exception()
    }
})
