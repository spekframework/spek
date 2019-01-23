package testData.package1

import org.spekframework.spek2.Spek

object SpekTestWithFailureDuringRootDiscoveryPhase : Spek({
    throw Exception()
})
