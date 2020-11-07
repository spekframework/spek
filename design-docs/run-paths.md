# Run paths

A run path represents which tests are run by Spek.  The most basic run path is `/` which means to run every test.

To run tests under a specific package, say `com.example`,  use the run path `/com/example` - basically replace all occurrences of `.` to a `/`. The same can be applied to running tests for a specific class. For the class `com.example.FooSpec` , use the run path `/com/example/FooSpec`. 

Given the example below, to run the test `it("do this")` use the run path `com/example/FooSpec/Some foo/do this`.

```kotlin
package com.example

object FooSpec: Spek({
	describe("Some foo") {
        it("do this") { ... }
        it("do that") { ... }
    }
})
```

## Relationships

Given the following structure:

```
com/
	example/
		FooSpec
		BarSpec
    sample/
    	FooBarSpec
```

If Spek is run with the run path `/com/example`, the test classes `FooSpec` and `BarSpec` will be executed but not `FooBarSpec` as it is not a child of `/com/example`. If the run path used was `/com` or `/`, then all test classes will be executed. 

The same rules above can be applied to scopes within a test class.

```kotlin
object FooSpec: Spek({
	describe("Some foo") {
        it("do this") { ... }
        it("do that") { ... }
    }
    
    describe("Another foo") {
        it("should do this") { ... }
    }
})
```

Running Spek with the run path `/com/example/FooSpec/Some foo`, will only execute the test scopes `it("do this")` and `it("do that")`. To run all test scopes under `FooSpec`, use the run path `/com/example/FooSpec`.

## Future enhancements

- Support ANT-like path wildcards (`**` and `*`) in the future when there is a need for it.
- `/` is a reserved character, escaping it can be supported later on.

