### Gradle
Tests can be run by just invoking the `test` task (`gradlew test`). Currently, there
is no way to run a specific test via Gradle.

### IDE 
To run tests in IntelliJ IDEA or Android Studio you need to install [Spek Framework plugin](https://plugins.jetbrains.com/plugin/10915-spek-framework) (search for `Spek Framework`).
The plugin will allow you to:

- Run all tests in a package (there should be an option under `Run` -> `Specs in <package>` when right clicking a package in the explorer)
- Run specific scope via the gutter icons.
  ![gutter_icons](./images/gutter_icons.png)