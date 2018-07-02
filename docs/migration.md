This section will guide you through migrating `1.x` tests to `2.x`.

Both versions implement (albeit differently) a JUnit Platform `TestEngine`. `1.x` has an unique id (engine name) `spek` while `2.x`
has `spek2`, this allows having `1.x` and `2.x` tests in the same project.

!!! info "Sample configuration for a gradle based project"
    ```groovy
    test {
        useJUnitPlatform {
            includeEngines 'spek', 'spek2'
        }
    }
    ```

## Maven coordinates
Spek is split into an API and a runtime (referred to as runner in `2.x`), replace `org.jetbrains.spek:spek-api` and 
`org.jetbrains.spek:spek-junit-platform-engine` with `org.spekframework.spek2:spek-dsl-jvm` and 
`org.spekframework.spek2:spek-junit5-runner`, respectively.

## Base package
The base package is now `org.spekframework.spek2`, but since most of the API has change it's not easy as replacing
`org.jetbrains.spek` with the new base package.

## DSL changes
`2.x` introduces two distinct and completely isolated testing styles: `specification` and `gherkin`, mixing and matching
different synonyms is not allowed.

### describe, context, it
This `1.x` style is the most straightforward to migrate, just add an import to
`org.spekframework.spek2.specification.describe`.

```kotlin
import org.spekframework.spek2.specifcation.describe
import org.spekframework.spek2.Spek

object MySpec: Spek({
    describe("Something") {
        ...
    }
})
```

### given, on, it
`2.x` introduces a complete gherkin like DSL and this style is roughly equivalent to it.

```kotlin
import org.spekframework.spek2.gherkin.Feature
import org.spekframework.spek2.Spek

object MyFeature: Spek({
    Feature("My feature") {
        Scenario("Some scenario") {
            Given("Some precondition") { ... }
            When("Some action") { ... }
            Then("Some result") { ... }
        }
    }
})
```


## Dropped extensions
### Subjects
TBD

### Table driven
TBD
