---
layout: other
title: Spek - Download
---

**Build Status**:
<a href="http://teamcity.jetbrains.com/viewType.html?buildTypeId=Spek_BuildAndTests">
<img src="http://teamcity.jetbrains.com/app/rest/builds/buildType:(id:Spek_BuildAndTests)/statusIcon"/>
</a>

### Source Code

If you want to download as source code, clone the [repository](https://github.com/jetbrains/spek}).

### Binaries

Download the latest artifacts from the [Build Server](http://teamcity.jetbrains.com/viewType.html?buildTypeId=Spek_BuildAndTests)

### Versions

| Artifact Version | Github Branch | Kotlin Version |
| ---------------- | ------------- | -------------- |
| [0.106](http://repository.jetbrains.com/simple/spek/org/spek/spek/0.1.106) | 0.1.m6.2 | M6.2 |
| [0.122](http://repository.jetbrains.com/simple/spek/org/spek/spek/0.1.122) | 0.1.m7 | M7 |
| [0.127](http://repository.jetbrains.com/simple/spek/org/spek/spek/0.1.127) | 0.1.m8 | M8 |
| [0.1-SNAPSHOT](http://repository.jetbrains.com/simple/spek/org/spek/spek/0.1-SNAPSHOT) | master | Kotlin Snapshot |

### Maven and Gradle

Spek has not been released yet and as such is not made available on Maven Central. However you can download it from the [JetBrains Artifactory](http://repository.jetbrains.com).

To use it in Maven insert the following in your pom.xml file:


{% highlight xml %}
 
 <dependency>
    <groupId>org.spek</groupId>
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
    testCompile 'org.spek:spek:$version'
}

{% endhighlight %}



