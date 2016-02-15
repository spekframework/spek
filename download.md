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
| [0.1.142](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.142) | 0.1.m9 | M9 | 
| [0.1.145](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.145) | 0.1.m10 | M10 | 
| [0.1.164](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.164) | 0.1.m12 | M12 0.12.200 | 
| [0.1.165](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.165) | 0.1.m12.213 | M12 0.12.213| 
| [0.1.175](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.175) | 0.1.m13 | M13 0.13.1513 | 
| [0.1.178](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.178) | 0.1.m14 | M14 0.14.449 |
| [0.1.182](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.182) | - | Beta 1 1.0.0-beta-1103 | 
| [0.1.186](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.186) | - | Beta 3 1.0.0-beta-3595 | 
| [0.1.188](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.188) | - | Beta 4 1.0.0-beta-4584 | 
| [0.1.195](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1.195) | - | 1.0.0|
| [0.1-SNAPSHOT](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/0.1-SNAPSHOT) | master | Kotlin 0.1-SNAPSHOT |

### Maven and Gradle

Spek has not been released yet and as such is not made available on Maven Central. However you can download it from the [JetBrains Artifactory](http://repository.jetbrains.com).

*Versions 0.1.106 to 0.1.127*

This is only applicable to version 0.1.106 through to 0.1.127. For Snapshot and higher versions, see below. 

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


*Versions after 0.1.127 and 0.1-SNAPSHOT*

Spek has moved packages to org.jetbrains.spek. As such, the Maven/Gradle packages have changed:

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
