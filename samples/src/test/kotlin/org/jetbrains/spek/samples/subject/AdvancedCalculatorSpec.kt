package org.jetbrains.spek.samples.subject

import org.jetbrains.samples.AdvancedCalculator
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.jetbrains.spek.subject.itBehavesLike
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
