# Setting up for JVM

## Gradle
Gradle `4.7` is recommended as it added a built-in support for JUnit Platform.

```groovy
// setup dependencies
dependencies {
    // some version of Kotlin
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"

    testImplementation ('org.spekframework.spek2:spek-dsl-jvm:${spek_version}')  {
        exclude group: 'org.jetbrains.kotlin'
    }
    testRuntimeOnly ('org.spekframework.spek2:spek-runner-junit5:${spek_version}') {
        exclude group: 'org.junit.platform'
        exclude group: 'org.jetbrains.kotlin'
    }

    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
}

// setup the test task
test {
    useJUnitPlatform {
        includeEngines 'spek2'
    }
}
```

## Maven
The JUnit Team has provided a basic plugin to run JUnit Platform tests in Maven. You can also check the [maven sample](https://github.com/spekframework/spek/tree/2.x/samples/maven) 
in the main repository.

!!! warning "Requirements"
    Please use Maven Surefire `2.22.0` with the `junit-platform-surefire-provider`.
```xml
...
<build>
    <plugins>
        ...
        <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.22.0</version>
        </plugin>
    </plugins>
</build>
...
<repositories>
    <repository>
        <id>spek-dev</id>
        <url>https://dl.bintray.com/spekframework/spek-dev</url>
    </repository>
</repositories>
<dependencies>
    ...
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-stdlib</artifactId>
        <version>${kotlin.version}</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.spekframework.spek2</groupId>
        <artifactId>spek-dsl-jvm</artifactId>
        <version>${spek_version}</version>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <groupId>org.jetbrains.kotlin</groupId>
                <artifactId>*</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.spekframework.spek2</groupId>
        <artifactId>spek-runner-junit5</artifactId>
        <version>${spek_version}</version>
        <exclusions>
            <exclusion>
                <groupId>org.jetbrains.kotlin</groupId>
                <artifactId>*</artifactId>
            </exclusion>
            <exclusion>
                <groupId>org.junit.platform</groupId>
                <artifactId>*</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-reflect</artifactId>
        <version>${kotlin.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
...
```

## Running tests
### Gradle
If you followed the previous section correctly, the tests can be run by just invoking the `test` task (`gradlew test`). Currently, there
is no way to run a specific test via Gradle.

### IntelliJ IDEA
To run tests in IntelliJ IDEA you need to install [Spek Framework plugin](https://plugins.jetbrains.com/plugin/10915-spek-framework) (search for `Spek Framework`).
The plugin will allow you to:

- Run all tests in a package (there should be an option under `Run` -> `Specs in <package>` when right clicking a package in the explorer)
- Run specific scope via the gutter icons.
  ![gutter_icons](./images/gutter_icons.png)