This fork of Spek is intended to address some of the problems with the existing version of the library.  

1. **Execution order** - Fixes bugs related to the execution order of `on` blocks (see [#39](https://github.com/JetBrains/spek/issues/39), [#32](https://github.com/JetBrains/spek/issues/32)) .  Improves setup/teardown features for tests: `describe` body blocks are executed before each `it`, and `afterEach` blocks are added.
1. **Test pollution** - Each `it` is created in a clean context, and all relevant setup code (the body of each parent `describe`) is executed for *each* test.  (see [#32](https://github.com/JetBrains/spek/issues/32))
1. **Simpler/more flexible DSL** - This reduces the overhead in writing simple tests, and allows for more sophisticated test structures, like arbitrary nesting depth
  - outer `init` is no longer necessary
  - `given`, `on` replaced with `describe`
  - `describe`s can be nested
  - removed assertions `should`, etc -- use kotlin's `assertEquals`, or other library ([AssertJ](http://joel-costigliola.github.io/assertj/), [HamKrest](https://github.com/npryce/hamkrest), etc) instead

## How to use
1. Download the [jar](https://github.com/lkogler/spek/releases)
2. Make a folder called testLibs in your project and add the jar
3. Update your `build.gradle`: Remove the current `spek` dependency, and add the following:

  ```groovy
  testCompile 'junit:junit:4.12'  // if not already present
  testCompile "org.jetbrains.kotlin:kotlin-test:${kotlin_version}"  // if not already present
  testCompile fileTree(dir: 'testLibs', include: ['*.jar'])
  ```

## Example

```kotlin
class ExampleTest : Spek({
    describe("a calculator") {
        val calculator = SampleCalculator()
        
        describe("addition") {
            val sum = calculator.sum(2, 4)
            
            it("should return the result of adding the first number to the second number") {
                assertEquals(6, sum)
            }
            
            // Example of a failing test
            it("should fail") {
                assertEquals(7, sum)
            }
        }
        
        describe("subtraction") {
            val subtract = calculator.subtract(4, 2)

            it("should return the result of subtracting the second number from the first number") {
                assertEquals(2, subtract)
            }
        }
    }
})
```

