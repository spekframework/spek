package testData.package1

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SpekTest2: Spek({
    describe("bar") {
        it("should do something") { }
        it("should do that") { }
    }
})