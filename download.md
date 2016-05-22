---
layout: default
title: Spek - Download
---

**Build Status**:
<a href="http://teamcity.jetbrains.com/viewType.html?buildTypeId=Spek_BuildAndTests">
<img src="http://teamcity.jetbrains.com/app/rest/builds/buildType:(id:Spek_BuildAndTests)/statusIcon"/>
</a>


Spek is currently under EAP for a 1.0 release. 

*Kotlin version: 1.0.2*


### Source Code

If you want to download as source code, clone the [repository](https://github.com/jetbrains/spek).

### Binaries

Download the latest artifacts from the [Build Server](http://teamcity.jetbrains.com/viewType.html?buildTypeId=Spek_BuildAndTests) or from
[JetBrains Artifactory](http://repository.jetbrains.com/simple/spek/org/jetbrains/spek/spek/)

### Maven and Gradle

You can download it from the [JetBrains Artifactory](http://repository.jetbrains.com).

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
