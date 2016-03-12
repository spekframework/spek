package org.jetbrains.spek.api

 interface SpekTestableComponent {
     fun description(): String
     fun run(action: () -> Unit)
}

 interface TestSpekAction : SpekTestableComponent {
     fun listGiven(): List<TestGivenAction>
}

 interface TestGivenAction : SpekTestableComponent {
     fun listOn(): List<TestOnAction>
}

 interface TestOnAction : SpekTestableComponent {
     fun listIt(): List<TestItAction>
}

 interface TestItAction : SpekTestableComponent {
}

