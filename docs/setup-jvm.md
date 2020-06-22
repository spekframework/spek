# Setting up for JVM

## Gradle
Gradle `4.7` or higher is recommended as it added a built-in support for JUnit Platform.

Groovy config:

```groovy
// setup dependencies
dependencies {
    // some version of Kotlin
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"

    testImplementation "org.spekframework.spek2:spek-dsl-jvm:$spek_version"
    testRuntimeOnly "org.spekframework.spek2:spek-runner-junit5:$spek_version"

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

Kotlin Config:

```kt
// setup dependencies
dependencies {
    // some version of Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek_version")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spek_version")

    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
}

// setup the test task
tasks.test {
    useJUnitPlatform {
        includeEngines("spek2")
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
        <artifactId>kotlin-stdlib-jdk8</artifactId>
        <version>${kotlin.version}</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.spekframework.spek2</groupId>
        <artifactId>spek-dsl-jvm</artifactId>
        <version>${spek_version}</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.spekframework.spek2</groupId>
        <artifactId>spek-runner-junit5</artifactId>
        <version>${spek_version}</version>
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
See [Running tests](running.md).
