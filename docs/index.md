# Spek Framework

Spek is a testing framework for Kotlin.

## Choose your style

### Specification
```kotlin
describe("A set") {
    val set by memoized { mutableSetOf<String>() }

    context("is empty") {
        it("should have a size of 0") {
            assertEquals(0, set.size)
        }

        it("should throw when first is invoked") {
            assertFailsWith(NoSuchElementException::class) {
                set.first()
            }
        }
    }
}
```

### Gherkin
```kotlin
Feature("Set") {
    val set by memoized { mutableSetOf<String>() }

    Scenario("is empty") {
        Then("should have a size of 0") {
            assertEquals(0, set.size)
        }

        And("should throw when first is invoked") {
            assertFailsWith(NoSuchElementException::class) {
                set.first()
            }
        }
    }
}
```
