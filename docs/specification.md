This style is inspired by test frameworks like `Jasmine` and `RSpec`.

```kotlin
object CalculatorSpec: Spek({
    describe("A calculator") {
        val calculator by memoized { Calculator() }

        describe("addition") {
            it("returns the sum of its arguments") {
                assertEquals(3, calculator.add(1, 2))
            }
        }
    }
})
```
## Concepts
### Suites
Defined by either calling `describe` or `context`, they are used to group related specs.

### Specs
Defined by calling `it`, is the place where you usually place your assertions/checks.

### Skipping
`describe`, `context` and `it` can be prefixed with an `x` which will tell Spek to skip them during execution.

### Aliases
This style also provides aliases to the built-in fixtures.

- *before* and *after* is equivalent to *beforeGroup* and *afterGroup*, respectively.
- *beforeEach* and *afterEach* is equivalent to *beforeEachTest* and *afterEachTest*, respectively.

## Best practices
### Validating a side-effect
`describe` should be use to _describe_ the action and an `it` to check the expected side-effect.
 
```kotlin
val set by memoized { mutableSetOf<String>() }

describe("adding an item") {
    beforeEachTest {
        set.add("item")
    }
    
    it("should contain item") {
        assertEquals("item", set[0])
    }
}
```

Use multiple `it`s if you need to check several side-effects.

```kotlin
val set by memoized { mutableSetOf<String>() }

describe("adding an item") {
    beforeEachTest {
        set.add("item")
    }
    
    it("should contain item") {
        assertEquals("item", set[0])
    }
    
    it("should have a size > 0") {
        assertTrue(set.size() > 0)
    }
}
```

### Validating a return value
Like the previous section, use `describe` to _describe_ the action, `it` check the expected value and a `lateinit` variable to store the
result of the action.

```kotlin
describe("adding 1 + 2") {
    var result: Int = 0
    beforeEachTest {
        result = 1 + 2
    }
    
    it("result should be 3") {
        assertEquals(result, 3)
    }
}
```
