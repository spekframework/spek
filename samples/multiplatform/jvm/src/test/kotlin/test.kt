import org.spekframework.spek2.Calculator
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object CalculatorSpec2: Spek({
    val calculator by memoized { Calculator() }

    describe("Calculator") {
        it("1 + 2 == 3") {
            assertEquals(3, calculator.add(1, 2))
        }
    }
})
