Before diving into writing tests, it is important to understand Spek works.

!!! info "Disclaimer"
    To make it simple, other features like **fixtures** and **memoized** are not discussed this section.

## Scopes
Tests are defined using nested lambdas where each lambda is considered a `scope`. There are 
3 types of scope provided.

- `group` is used to represent some logical grouping of scopes.
- `test` is where checks and assertions are made.
- `action` is similar to a test but can contain other tests.

## Phases
Spek has two phases, `discovery` and `execution`. During discovery, the test tree is built
by invoking all group scopes starting at the root while all other scope types are **collected**.
After the the test is tree is build, the execution phase starts. In this phase, the test tree is traversed and 
the scopes **collected** in the previous phase are invoked.

To make it easier to understand, consider the test below.

```kotlin
object MySpec: Spek({
    group("a group") {
        println("I'm in 'a group'")
        group("some nested group") {
            println("I'm in 'some nested group'")
            test("a test") {
                println("I'm in 'a test'")
            }
        }
        
        action("some action") {
            println("I'm in 'some action'")
            test("a test within an action") {
                println("I'm in 'a test within an action'")
            }
            
            test("another test within an action") {
                println("I'm in 'another test within an action'")
            }
        }
    }
})
```

Running it will yield the following output:

``` hl_lines="1 2"
I'm in 'a group'
I'm in 'some nested group'
I'm in 'a test'
I'm in 'some action'
I'm in 'a test within an action'
I'm in 'another test within an action'
```

The lines highlighted are printed during discovery, while the remaining lines during execution.

## Conclusion
Since group scopes are **eagerly** invoked during the discovery phase, it is not recommended to have any
test initialization within them. Test initialization should be done using **memoized** and/or
**fixtures**, which are discussed in the next section.
