# Setting up for JVM

## Gradle
Gradle `4.7` is recommended as it added a built-in support for JUnit Platform.

```groovy
// setup dependencies
dependencies {
    // some version of Kotlin
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

    testImplementation ('org.spekframework.spek2:spek-dsl-jvm:2.x.x')  {
        exclude group: 'org.jetbrains.kotlin'
    }
    testRuntimeOnly ('org.spekframework.spek2:spek-junit5-runner') {
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
    Please use Maven Surefire `2.21.0` with the `junit-platform-surefire-provider`.
```xml
...
<build>
    <plugins>
        ...
        <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.21.0</version>
            <dependencies>
                <dependency>
                    <groupId>org.junit.platform</groupId>
                    <artifactId>junit-platform-surefire-provider</artifactId>
                    <version>{junit.platform.version}</version>
                </dependency>
                <dependency>
                    <groupId>org.spekframework.spek2</groupId>
                    <artifactId>spek-junit5-runner</artifactId>
                    <version>2.x.x</version>
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
            </dependencies>
        </plugin>
    </plugins>
</build>
...
<dependencies>
    ...
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-stdlib</artifactId>
        <version>{kotlin.version}</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.spekframework.spek2</groupId>
        <artifactId>spek-dsl-jvm</artifactId>
        <version>2.x.x</version>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <groupId>org.jetbrains.kotlin</groupId>
                <artifactId>*</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-reflect</artifactId>
        <version>{kotlin.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
...
```

## IntelliJ Plugin
`2.x` requires a new plugin to be installed, TBD.
