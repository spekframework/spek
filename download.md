---
layout: other
title: Spek - Download
---

**Build Status**:
<a href="http://teamcity.jetbrains.com/viewType.html?buildTypeId=Spek_BuildAndTests">
<img src="http://teamcity.jetbrains.com/app/rest/builds/buildType:(id:Spek_BuildAndTests)/statusIcon"/>
</a>

### Source Code

If you want to download as source code, clone the [repository](https://github.com/jetbrains/spek).

### Binaries

Download the latest artifacts from the [Build Server](http://teamcity.jetbrains.com/viewType.html?buildTypeId=Spek_BuildAndTests)

### Versions

| Artifact Version | Github Branch | Kotlin Version |
| ---------------- | ------------- | -------------- |
| [0.1.195](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.195) | - | 1.0.0|
| [0.1-SNAPSHOT](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1-SNAPSHOT) | master | Kotlin 0.1-SNAPSHOT |

### Maven and Gradle

Spek has not been released yet and as such is not made available on Maven Central. However you can download it from the [JetBrains Artifactory](http://repository.jetbrains.com).

To use it in Maven insert the following in your pom.xml file:

{% highlight xml %}
 
 <dependency>
    <groupId>org.jetbrains.spek</groupId>
    <artifactId>spek</artifactId>
    <version>$version</version>
    <type>pom</type>
    <scope>test</scope>
 </dependency>

 <repositories>
    <repository>
      <id>jebrains-all</id>
      <url>http://repository.jetbrains.com/all</url>
    </repository>
  </repositories>

{% endhighlight %}


For Gradle:

{% highlight xml %}

repositories {
    maven {
        url "http://repository.jetbrains.com/all"
    }
}

dependencies {
    testCompile 'org.jetbrains.spek:spek:$version'
}

{% endhighlight %}
