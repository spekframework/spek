This style is inspired by test frameworks like `Jasmine` and `RSpec`.

```kotlin
object CalculatorSpec: Spek({
    describe("A calculator") {
        val calculator by memoized { Calculator() }

        describe("addition") {
            it("returns the sum of its arguments") {
                assertThat(3, calculator.add(1, 2))
            }
        }
    }
})
```

## Suites
Defined by either calling `describe` or `context`, they are used to group related specs.

## Specs
Defined by calling `it`, is the place where you usually place your assertions/checks.

## Skipping
`describe`, `context` and `it` can be prefixed with an `x` which will tell Spek to skip them during execution.

## Aliases
This style also provides aliases to the built-in fixtures.

- *before* and *after* is equivalent to *beforeGroup* and *afterGroup*, respectively.
- *beforeEach* and *afterEach* is equivalent to *beforeEachTest* and *afterEachTest*, respectively.