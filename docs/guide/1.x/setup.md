Spek is implemented as a [JUnit Platform](https://junit.org/junit5) TestEngine. 

Include `org.jetbrains.spek:spek-api` and `org.jetbrains.spek:spek-junit-platform-engine` 
in the test classpath. The former is needed during compilation, while the latter during runtime only.

## Gradle
Gradle `4.7` is recommended as it added a built-in support for JUnit Platform.

```groovy
// setup dependencies
dependencies {
    // some version of Kotlin
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

    testImplementation ('org.jetbrains.spek:spek-api:1.x.x')  {
        exclude group: 'org.jetbrains.kotlin'
    }
    testRuntimeOnly ('org.jetbrains.spek:spek-junit-platform-engine:1.x.x') {
        exclude group: 'org.junit.platform'
        exclude group: 'org.jetbrains.kotlin'
    }
    
    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
}

// setup the test task
test {
    useJUnitPlatform {
        includeEngines 'spek'
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
                    <groupId>org.jetbrains.spek</groupId>
                    <artifactId>spek-junit-platform-engine</artifactId>
                    <version>1.x.x</version>
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
        <groupId>org.jetbrains.spek</groupId>
        <artifactId>spek-api</artifactId>
        <version>1.x.x</version>
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

## IDE Plugin
Installing the [plugin](https://plugins.jetbrains.com/plugin/8564-spek) will allow
running tests within IntelliJ IDEA and Android Studio.

!!! warning "Limitations"
    The plugin won't work when using JUnit Platform's `@RunWith` (`junit-vintage-engine`) support.
