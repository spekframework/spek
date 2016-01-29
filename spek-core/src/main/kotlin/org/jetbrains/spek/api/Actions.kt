package org.jetbrains.spek.api

public interface SpekTestableComponent {
    public fun description(): String
    public fun run(action: () -> Unit)
}

public interface TestSpekAction : SpekTestableComponent {
    public fun listGiven(): List<TestGivenAction>
}

public interface TestGivenAction : SpekTestableComponent {
    public fun listOn(): List<TestOnAction>
}

public interface TestOnAction : SpekTestableComponent {
    public fun listIt(): List<TestItAction>
}

public interface TestItAction : SpekTestableComponent {
}

