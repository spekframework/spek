# Breaking changes
The following are breaking changes in `2.x`.

## Coordinates and package prefix
Spek now uses `org.spekframework.spek2` as its base package prefix and maven group id.

## DSL changes
DSL is now split into two distinct styles: `specification` and `gherkin`. 

## JUnit Platform
`JUnit Platform` is now only used as a runner, instead of driving the whole discovery and execution phases. It now
uses `spek2` as the test engine name to allow having `1.x` and `2.x` tests in the same project. This will pave the
way to support Kotlin multiplatform projects.

## Removed Extensions
The following extensions are removed.

- `spek-subject-extension`
- `spek-data-driven-extension`

## Removed APIs
- `Spek.include`, introduced only to support the subject extension.
- `action` scope is dropped to simplify the API.
