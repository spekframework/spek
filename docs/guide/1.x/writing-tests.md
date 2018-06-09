Before proceeding, it is recommended to read [this](preamble.md) section.

## Styles
Spek provides two styles when writing tests, although not strictly enforced it is 
not recommended to mix and match different styles.

### given, on, it
This is the classic style and what the first version of Spek was based on. The idea is quite straightforward:

- `given`: which defines the context, that is, the conditions under which I’m testing. Can be repeated any number of times.

- `on`: which defines the action, that is, what is executing for the test to later verify. Can be repeated any number of times for each given.

- `it`: which defines the actual tests, that is, what to be verified once the action is executed. Can be repeated any number of times for each on.

In the case of a calculator, this would be:

```kotlin
object CalculatorSpec: Spek({
    given("a calculator") {
        val calculator by memoized { SampleCalculator() }
        on("2 + 4") {
            val sum = calculator.sum(2, 4)
            it("should be 6") {
                assertEquals(6, sum)
            }
        }
        on("4 - 2") {
            val subtract = calculator.subtract(4, 2)
            it("should be 2") {
                assertEquals(2, subtract)
            }
        }
    }
})
```

!!! important "Implementation"
    `given`, `on` and `it` are synonyms of `group`, `action` and `test`, respectively.  Using only the core DSL, 
    the test above is equivalent to:
    ```kotlin
    object CalculatorSpec: Spek({
        group("a calculator") {
            val calculator by memoized { SampleCalculator() }
            action("2 + 4") {
                val sum = calculator.sum(2, 4)
                test("should be 6") {
                    assertEquals(6, sum)
                }
            }
            action("4 - 2") {
                val subtract = calculator.subtract(4, 2)
                test("should be 2") {
                    assertEquals(2, subtract)
                }
            }
        }
    })
    ```

### describe, it
This style is heavily inspired by **Jasmine** and **Mocha** test frameworks.

- `describe`: which defines the context of what we’re testing. We can nest as many 
   describes as we want. Each one should provide more context
- `it`: which defines the actual tests, that is, what to be verified once the action 
   is executed. Can be repeated any number of times for each describe

Using this style, the test in the previous section is as follows:

```kotlin
object SimpleSpec: Spek({
    describe("a calculator") {
        val calculator by memozed { SampleCalculator() }
        
        describe("2 + 4") {
            val sum by memoized { calculator.sum(2, 4) }

            it("should be 6") {
                assertEquals(6, sum)
            }
        }

        describe("4 - 2") {
            val difference by memoized { calculator.subtract(2, 4) }
            it("should be 2") {
                assertEquals(2, difference)        
            }
        }
    }
})
```

!!! important "Implementation"
    `describe` and `it` are synonyms of `group` and `test`, respectively. Using only the core DSL, 
    the test above is equivalent to:
    ```kotlin
    object SimpleSpec: Spek({
        group("a calculator") {
            val calculator by memozed { SampleCalculator() }
            
            group("2 + 4") {
                val sum by memoized { calculator.sum(2, 4) }
                test("should be 6") {
                    assertEquals(6, sum)
                }
            }
    
            group("4 - 2") {
                val difference by memoized { calculator.subtract(2, 4) }
                test("should be 2") {
                    assertEquals(2, difference)        
                }
            }
        }
    })
    ```
