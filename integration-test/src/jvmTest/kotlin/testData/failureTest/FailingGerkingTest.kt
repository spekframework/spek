package testData.failureTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object FailingGerkingTest : Spek({
  Feature("a feature") {
    Scenario("scenario") {
      Given("failing to setup") {
        throw Exception()
      }

      When("do something") {
        println("test")
      }
    }
  }
})
