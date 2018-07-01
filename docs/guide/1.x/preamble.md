Before diving into writing tests, it is important to understand Spek works.

## Scopes
Tests are defined using nested lambdas where each lambda is considered a `scope`. There are 
3 types of scope provided.

- `group` is used to represent some logical grouping of scopes.
- `test` is where checks and assertions are made.
- `action` is similar to a test but can contain other tests.

## Phases

### Discovery
During discovery, the test tree is built by invoking  all group scopes (starting at the root) while other scope types, 
fixtures and memoized values are **collected**. The test tree is composed of, the invoked group scopes as non-leaf 
nodes and the **collected** scope types as leaf nodes. Fixtures and memoized values are stored within 
the scope they are declared.

!!! important "Note for action scopes"
    Since action scopes are not invoked during this phase, test scopes declared within them are not
    part of the test tree yet, but hey are dynamically added during execution phase.

### Execution
In this phase, the test tree is traversed (starting from the root). The following snippet is an overview of how
this phase is implemented.

```kotlin
fun execute(scope: Scope) {
    invokeBeforeFixtures(scope)
    when {
        scope is Group -> scope.children.forEach(::execute)
        scope is Action -> invokeAction(scope)
        scope is Test -> invokeTest(scope)
    }
    invokeAfterFixtures(scope)
}
```
Test scopes declared within action scopes are invoked immediately as they are encountered. To make it easier to
 understand, consider the test below.

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

!!! warning "Execution order"
    Execution order of scopes declared in a group scope (including the root) is **not** guaranteed.
    
### Fixtures
Each scope by design have *before* and *after* execution hooks, which can be attached to using fixtures and invoked during 
the execution phase.

- `beforeGroup` and `afterGroup` can be used to run arbitrary code before and after a group is executed, respectively.
- `beforeEachTest` and `afterEachTest` can be used to run arbitrary code before and after a action or test is executed, 
   respectively.

!!! important "A note for test scopes"
    Test scopes declared within action scopes can't have execution hooks attached to them (due to how action scopes are 
    implemented), so `beforeEachTest` and `afterEachTest` can't be used.
    
## Memoized values
TBD


## Conclusion
Since group scopes are **eagerly** invoked during the discovery phase, it is not recommended to have any
test initialization within them. Test initialization should be done using **memoized** and/or
**fixtures**, which are discussed in the next section.
