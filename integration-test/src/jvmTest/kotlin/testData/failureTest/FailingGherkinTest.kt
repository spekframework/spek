package testData.failureTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object FailingGherkinTest : Spek({
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

object FailingGherkinWithManyScenariosTest : Spek({
  Feature("a feature") {
    Scenario("scenario 1") {
      Given("failing to setup") {
        throw Exception()
      }

      When("do something") {
        println("test")
      }
    }

    Scenario("scenario 2") {
      Given("failing to setup") {
        throw Exception()
      }

      When("do something") {
        println("test")
      }
    }
  }
})

object FailingGherkinAfterSuccessfulSetup : Spek({
  Feature("a feature") {
    Scenario("scenario 1") {
      Given("failing to setup") {
        println("pass")
      }

      When("do something") {
        throw Exception()
      }

      Then("assertion") {
        println("assert")
      }
    }
  }
})