This section will guide you through migrating `1.x` tests to `2.x`.

Both versions implement (albeit differently) a JUnit Platform `TestEngine`. `1.x` has an unique id (engine name) `spek` while `2.x`
has `spek2`, this allows having `1.x` and `2.x` tests in the same project.

!!! info "Sample configuration for a gradle based project"
    ```groovy
    test {
        useJUnitPlatform {
            includeEngines 'spek', 'spek2', 'some-other-test-engine'
        }
    }
    ```

## Maven coordinates
The table below shows the equivalent `1.x` and `2.x` artifacts.

| 1.x                                             | 2.x                                          |
|-------------------------------------------------|----------------------------------------------|
| `org.jetbrains.spek:spek-api`                   | `org.spekframework.spek2:spek-dsl-jvm`       |
| `org.jetbrains.spek:spek-junit-platform-engine` | `org.spekframework.spek2:spek-runner-junit5` |

## Base package
The base package is now `org.spekframework.spek2`, but since most of the API has change it's not easy as replacing
`org.jetbrains.spek` with the new base package.

## DSL changes
`2.x` introduces two distinct and completely isolated testing styles: `specification` and `gherkin`, mixing and matching
different synonyms is not allowed.

### describe, context, it
This `1.x` style is the most straightforward to migrate, adding an import to
`org.spekframework.spek2.specification.describe` should work in most cases. However, a major difference is `context` and `it` are not allowed in the root
scope, you have to either wrap them in a `describe` or for `context` rename it to `describe`.

```kotlin
object SetSpec: Spek({
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
  })
```

For example, the `2.x` equivalent for the test above is:

```kotlin
import org.spekframework.spek2.style.specification.describe
import org.spekframework.spek2.Spek

object SetSpec: Spek({
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
})
```

### given, on, it
`2.x` introduces a complete gherkin like DSL and this style is roughly equivalent to it.

```kotlin
object SetSpek: Spek({
    val set by memoized { ... }

    // testing side-effects
    given("an empty set)" {
        beforeEachTest {
            // assume(list.isEmpty())
        }

        on("set.size") {
            val size = set.size
            it("should have a size of 0") {
                assertEquals(0, size)
            }
        }

        on("set.first()") {
            it("should throw an exception") {
                assertFailsWith(NoSuchElementException::class) {
                        set.first()
                }
            }
        }

        on("adding items") {
            set.add("foo")

            it("should have a size of 1") {
                assertEquals(1, set.size)
            }

            it("should contain foo") {
                assertTrue(set.contains("foo"))
            }
        }
    }

    // testing a return value
    given("a set with one item") {
        val item = "foo"
        beforeEachTest {
            set.add(item)
        }

        on("getting the first item") {
            val result = set.first()

            it("should return the first item") {
                assertEquals(item, result)
            }
        }
    }
})
```

The test above is equivalent to:

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
            Given("an empty set") {
                // assume(set.isEmpty())
            }
            Then("should have a size of 0") {
                assertEquals(0, set.size)
            }

            Then("should throw when first is invoked") {
                assertFailsWith(NoSuchElementException::class) {
                    set.first()
                }
            }
        }

        Scenario("getting first item") {
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


## Dropped extensions
### Subjects
For tests not using `include`, just replace `subject { ... }` to `memoized { ... }`.

```kotlin
object CalculatorTest: Spek({
    val subject by memoized { Calculator() }
    
    ...
})
```

If you do use `include`, you can now use `memoized` to reference scope values declared earlier.

```kotlin
object CalculatorSpecs: Spek({
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
})
```
