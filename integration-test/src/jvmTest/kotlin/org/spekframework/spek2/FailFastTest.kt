package org.spekframework.spek2

import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object FailFastTest: AbstractSpekTest({ helper ->
  describe("failed tests") {
    it("should not execute subsequent tests") {
      val recorder = helper.executeTest(testData.failureTest.FailingGerkingTest)

      assertEquals(1,  recorder.events().count { it is ExecutionEvent.Test })
    }
  }
})
