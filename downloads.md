---
layout: default
title: Spek - Releases
---

## Releases

**Current release**: 

[![Download](https://api.bintray.com/packages/jetbrains/spek/spek/images/download.svg) ](https://bintray.com/jetbrains/spek/spek/{{ site.data.releases.latest.build }})

Version: <a href="{{ site.baseurl }}/notes/{{ site.data.releases.latest.version }}.html">{{ site.data.releases.latest.version }} (Build {{ site.data.releases.latest.build }})</a><br/>
Date: {{ site.data.releases.latest.date }}<br/>
Kotlin version: {{ site.data.releases.latest.kotlinVersion }}

**Prior releases**

<ul>
{% for entry in site.data.releases.list %}
<li>{{ entry.date }} - <a href="{{ site.baseurl}}/{{ entry.notes }}/{{ entry.version }}">{{ entry.version }}</a> Build: {{ entry.build }}. Kotlin: {{ entry.kotlinVersion }}</li>
{% endfor %}
</ul>

## Fire hose

Latest published EAP
[![Download](https://api.bintray.com/packages/jetbrains/spek/spek/images/download.svg) ](https://bintray.com/jetbrains/spek/spek/_latestVersion)

<a href="http://teamcity.jetbrains.com/viewType.html?buildTypeId=Spek_BuildAndTests">
<img src="http://teamcity.jetbrains.com/app/rest/builds/buildType:(id:Spek_BuildAndTests)/statusIcon"/>
</a>

The master branch on GitHub is the active development branch. Stability is not guaranteed. 
 
### Source Code

If you want to download as source code, clone the [repository](https://github.com/jetbrains/spek).

### Binaries

Download the latest artifacts from the [Build Server](https://teamcity.jetbrains.com/viewLog.html?buildId=853167&tab=buildResultsDiv&buildTypeId=Spek_BuildAndTests)


### Maven and Gradle

Spek Releases are available on [Bintray](https://bintray.com/jetbrains/spek/spek) and [Maven Central](http://search.maven.org/).

Spek EAP's (i.e. Firehose) are only available on [Bintray](https://bintray.com/jetbrains/spek/spek). Please make sure you add the Bintray Repository if using EAP's. 

For more information on how to set up your project using Maven or Gradle, please see [Setting up](http://jetbrains.github.io/spek/docs/latest/#_setting_up) in the documentation.

