package testData
import org.spekframework.spek2.Spek

object TestWithFailure: Spek({
    test("failing test") {
        assert(false)
    }
})
