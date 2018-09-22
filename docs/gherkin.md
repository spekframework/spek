This style is inspired by Cucumber's [Gherkin](https://docs.cucumber.io/gherkin/) syntax.

```kotlin
object SetFeature: Spek({
    Feature("Set") {
        val set by memoized { mutableSetOf<String>() }

        Scenario("adding items") {
            When("adding foo") {
                set.add("foo")
            }

            Then("it should have a size of 1") {
                assertEquals(1, set.size)
            }

            Then("it should contain foo") {
                assertTrue(set.contains("foo"))
            }
        }

        Scenario("empty") {
            Then("should have a size of 0") {
                assertEquals(0, set.size)
            }

            Then("should throw when first is invoked") {
                assertFailsWith(NoSuchElementException::class) {
                    set.first()
                }
            }
        }

        Scenario("getting the first item") {
            val item = "foo"
            Given("a non-empty set")  {
                set.add(item)
            }

            lateinit var result: String

            When("getting the first item") {
                result = set.first()
            }

            Then("it should return the first item") {
                assertEquals(item, result)
            }
        }
    }
})

```

## Feature
Purpose is to provide a high-level description of a software feature, and to group related scenarios.

## Scenario
Describes a business rule, it consists of a list of steps.

## Steps
### Given
Describes the initial context or state of the scenario.

### When
Describes an event or action.

### Then
Describes the expected outcome or result.

### And
Can be used as an alternative to `Given`, `When`, and `Then` when you have more than one of those steps.
