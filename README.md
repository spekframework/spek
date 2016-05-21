Spek is a BDD style test framework for Kotlin which includes the following high-level features:

1. Arbitrarily nestable `describe` blocks for grouping tests with a shared context (also available under the aliases `context`, `given`, and `on`)
1. Setup and teardown using `beforeEach` and `afterEach` blocks, which will be run before/after each `it` in the `describe`
1. The ability to mark tests as pending by using an `x`- prefix (e.g. `xdescribe`)
1. The ability to run only a subset of "focused" tests by using an `f`- prefix (e.g. `fit`)
1. Integration with Junit (and IntelliJ via IntelliJ's Junit integration)
1. A standalone, junit-independent console runner

## How to use
Add the following line to your build.gradle

  ```groovy
  testCompile 'org.jetbrains.spek:spek:1.0.0'
  ```

## How to use the console runner
1. Build by running `./gradlew installApp`
2. The executable script will be found at `spek-dist/build/install/spek/bin/spek`
3. To invoke the console runner, you need both the “path” and the “package” arguments.  The “path” should only be the part of the path up until it matches the package path.  For example: `spek-dist/build/install/spek/bin/spek spek-samples/build/classes/main org.jetbrains.spek.samples`

## Example

```kotlin
class ExampleTest : Spek({
    describe("a calculator") {
        val calculator = SampleCalculator()
        var result = 0

        describe("addition") {
            beforeEach {
                result = calculator.sum(2, 4);
            }

            it("should return the result of adding the first number to the second number") {
                assertEquals(6, result)
            }
            it("should fail") {
                assertEquals(7, result)
            }
        }

        describe("subtraction") {
            beforeEach {
                result = calculator.subtract(4, 2)
            }

            it("should return the result of subtracting the second number from the first number") {
                assertEquals(2, result)
            }
        }
    }
})

```
Please see the [samples folder](https://github.com/JetBrains/spek/tree/1.0/spek-samples/src/main/kotlin/org/jetbrains/spek/samples) for more examples

