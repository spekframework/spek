Spek provides two built-in testing styles, but before proceeding to that - it is important to have a good
grasp on some core concepts.

## Structure
Tests are written using nested lambdas, each scope (level) can either be a `group` or a `test`.

```kotlin
object MyTest: Spek({
    group("a group") {
        test("a test") {
            ...
        }

        group("a nested group") {
            test("another test") {
                ...
            }
        }
    }
})
```

Understanding the difference between each scope is crucial in writing correct tests.

- **Test** scope is where you place your assertions/checks (in JUnit dialect - this is your test method).
- **Group** scope is used to organize your tests. It can contain test scopes and other group scopes as well.
  A very important thing to note is that group scopes will be **eagerly** executed during the *discovery phase*.
  (more about this on the next section).

## Phases
### Discovery
The goal of this phase is to build the test tree which outlines how the tests are executed. In order to achieve this, Spek
will execute all group scopes starting from the root. Consider the following test:

```kotlin
object MyTest: Spek({
    println("this is the root")
    group("some group") {
        println("some group")
        test("some test") {
            println("some test")
        }
    }

    group("another group") {
        println("another group")
        test("another test") {
            println("another test")
        }
    }
})
```
The output of the test is:
```text hl_lines="1 2 3"
this the root
some group
another group
some test
another test
```
The lines highlighted are printed during the discovery phase, while the rest in the execution phase. It is not recommended
to directly initialize test value(s) or do any setup in this scope as it only executed once during the discovery phase. Fret not,
Spek provides utilities to help you to those things properly.

### Execution
In this phase the tests are executed. Spek traverses the test tree starting from the root and for each scope it will execute
the registered *fixtures* (explained in the next section).

## Fixtures
Fixtures are used to execute a piece of code before or after each scope, this is the recommended way to do any test setup.
```kotlin
object MyTest: Spek({
    beforeGroup {
        println("before root")
    }

    group("some group") {
        beforeEachTest {
            println("before each test")
        }
        test("some test") {
            println("some test")
        }

        test("another test") {
            println("another test")
        }

        afterEachTest {
            println("after each test")
        }
    }

    afterGroup {
        println("after root")
    }
})
```

The output of the test above is (lines are highlighted to differentiate lines printed in fixtures):

```text hl_lines="3 6"
before root
before each test
some test
after each test
before each test
another test
after each test
after root
```

## Scope values
As a best practice you typically want test values to be unique for each test this can be done by using a `lateinit` variable
and assigning it within a `beforeEachTest`.

```kotlin
lateinit var calculator: Calculator

beforeEachTest {
    calculator = Calculator()
}
```

To make it more concise, Spek provides `memoized` to do the same thing:

```kotlin
val calculator by memoized { Calculator() }
```

`memoized` should not be used to hold the result of an action, use a fixture instead.

```kotlin
// BAD
val item by memoized { 1 }
val added by memoized { list.add(item) }

// GOOD
val item by memoized { 1 }

lateinit var added: Boolean
beforeEachTest {
    added = list.add(item)
}
```

### Caching modes
You can pass in an optional parameter to `memoized` which controls how the values are cached.

- `CachingMode.TEST`: each test scope will receive a unique instance, this is the default.
- `CachingMode.GROUP`: each group scope will receive a unique instance.
- `CachingMode.SCOPE`: effectively a singleton.
- `CachingMode.INHERIT`: internal use only.

If you are using the [gherkin](gherkin.md) style note that the default caching mode is `CachingMode.GROUP`.