package org.spekframework.spek2

import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertFalse

object FailFastTest : AbstractSpekTest({ helper ->
  describe("failed tests") {
    it("should not execute subsequent tests") {
      val recorder = helper.executeTest(testData.failureTest.FailingGherkinTest)

      assertEquals(1, recorder.events().count { it is ExecutionEvent.Test })
    }

    it("should ignore subsequent tests") {
      val recorder = helper.executeTest(testData.failureTest.FailingGherkinTest)

      assertEquals(1, recorder.events().count { it is ExecutionEvent.TestIgnored })
    }

    it("should fail") {
      val recorder = helper.executeTest(testData.failureTest.FailingGherkinTest)

      assertFalse(
          recorder.events()
              .filterIsInstance<ExecutionEvent.Test>()
              .first()
              .success
      )
    }

    it("should execute all scenarios") {
      val recorder = helper.executeTest(testData.failureTest.FailingGherkinWithManyScenariosTest)

      assertEquals(2, recorder.events().count { it is ExecutionEvent.Test })
    }

    it("should execute until failure") {
      val recorder = helper.executeTest(testData.failureTest.FailingGherkinAfterSuccessfulSetup)

      assertEquals(2, recorder.events().count { it is ExecutionEvent.Test })
      assertEquals(1, recorder.events().count { it is ExecutionEvent.Test && it.success })
    }
  }
})
