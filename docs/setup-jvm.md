# Setting up for JVM

## Gradle
Gradle `4.7` is recommended as it added a built-in support for JUnit Platform.

```groovy
repositories {
    maven { url "https://dl.bintray.com/spekframework/spek-dev" }
}

// setup dependencies
dependencies {
    // some version of Kotlin
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"

    testImplementation ('org.spekframework.spek2:spek-dsl-jvm:2.0.0-alpha.1')  {
        exclude group: 'org.jetbrains.kotlin'
    }
    testRuntimeOnly ('org.spekframework.spek2:spek-runner-junit5:2.0.0-alpha.1') {
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
The JUnit Team has provided a basic plugin to run JUnit Platform tests in Maven.

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
        <version>2.0.0-alpha.2</version>
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
        <version>2.0.0-alpha.2</version>
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

## IntelliJ Plugin
The new [plugin](https://plugins.jetbrains.com/plugin/10915-spek-framework) is available at IntelliJ's plugin repository (search for `Spek Framework`).
