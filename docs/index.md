# Spek Framework
Say goodbye to boilerplate and write meaningful tests.

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

## Lightweight
Spek does not provide any built-in support for mocking and assertion, you have the freedom to choose what you want.

## Re-usable tests
```kotlin
fun Suite.behavesLikeACalculator() {
    val calculator by memoized<Calculator>()

    it("1 + 2 = 3") {
        assertEquals(3, calculator.add(1, 2))
    }
}

describe("Calculator") {
    val calculator by memoized { Calculator() }

    behavesLikeACalculator()
}

describe("AdvancedCalculator") {
    val calculator by memoized { AdvancedCalculator() }

    behavesLikeACalculator()

    it("2 ^ 3 = 8") {
        assertEquals(8, calculator.pow(2, 3))
    }
}
```
