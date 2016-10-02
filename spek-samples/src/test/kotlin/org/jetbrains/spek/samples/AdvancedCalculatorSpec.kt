package org.jetbrains.spek.samples

import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.itBehavesLike
import kotlin.test.assertEquals

/**
 * @author Ranie Jade Ramiso
 */
class AdvancedCalculatorSpec : SubjectSpek<AdvancedCalculator>({
    subject { AdvancedCalculator() }

    itBehavesLike(CalculatorSpec::class)

    describe("pow") {
        it("should return the power of base raise to exponent") {
            assertEquals(subject.pow(2, 2), 4)
        }
    }
})
