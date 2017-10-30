package org.spekframework.spek2.samples.subject

import org.spekframework.spek2.samples.AdvancedCalculator
import org.spekframework.spek2.subject.SubjectSpek
import org.spekframework.spek2.subject.itBehavesLike
import kotlin.test.assertEquals

object AdvancedCalculatorSpec: SubjectSpek<AdvancedCalculator>({
    subject { AdvancedCalculator() }
    itBehavesLike(CalculatorSpec)

    describe("pow") {
        it("should return the power of base raise to exponent") {
            assertEquals(subject.pow(2, 2), 4)
        }
    }
})
