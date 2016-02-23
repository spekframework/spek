---
layout: other
title: Spek - Documentation
---


### Why a Specification Framework?

Tests are not only to check the code you've written executes and works, but also that it works as it should, that is, that as a developer
what's been implemented matches the requirements.

A green test doesn't tell you that the code correctly implements the specification. The user does, once they try it. However, we can try and make sure
we've correctly understood the specifications by contrasting the code with the specs. And what better place to have these specs to lookup than in the same
place we write the test code?

That's what Spek does. It is a specification framework that allows you to easily define specifications in a clear, understandable, human readable way.

You call them tests. We call them specifications.

### Why Kotlin?

[Kotlin](http://kotlinlang.org) is statically typed OSS language developed (primarily) and sponsored by JetBrains. It offers many advantages over Java such as its conciseness, expressiveness
and the ability to create nice DSL. It's 100% compatible with Java which means you can call Java from Kotlin and Kotlin from Java. And it's also easy to pick up.

Spek is written in Kotlin and specifications you write will be written in Kotlin. However, your SUT (System under Test) can be Java or Kotlin. As such
you can easily use Kotlin for your specifications. Yes, you call them tests. Start calling them specifications.


#### Spek and JUnit

You might notice that JUnit ships with Spek. Why? Because we rely on it for test runners, etc (for now).

### Usage

Your specifications need to inherit from the Spek() base class. The primary reason for this is so that we leverage a lot of the functionality of JUnit.

{% highlight kotlin %}
class TaxCalculatorSpecs: Spek() { init { 

    given("Tax rate calculator with default locale settings") {
        val taxRateCalculator = TaxRateCalculator()
        on("calculating the rate for an income of 200 and an average change of 10 per semester") {
            val value = taxRateCalculator.calculateRate(200, 10)
            it("should result in a value of 300") {
                assertEquals(300, value)
            }
        }
    }
}}
{% endhighlight %}

Each specification consists of:

* **given**: Only one of these should be present. Given indicates the context, the setup. Think of it as the arrange in the arrange, act, assert setup.
* **on**: One or more of these. Each on is an action. The act. More than one on indicates that they share the same context.
* **it**: One or more of these. Each it is a consequence of the action. The assert. Normally it's good practice to have a single assertion per it.

### Running Tests

**Console Runner**

Spek ships with a console runner that can be used via a built-in script file on OSX/Linux (spek) or Windows (spek.bat), located in the bin folder. Execute it without
parameters for more usage information.

The runner also has the option to output HTML.

**IntelliJ IDEA Test Runner**

If you're using IntelliJ IDEA, you can simply run Spek from inside the IDE

**TeamCity Test Runner**

Since it uses JUnit under the covers, TeamCity already supports Spek. And like TeamCity, other CI servers that support JUnit should too.


### FAQ

**Q: What is Kotlin?**
<br/>[Kotlin](http://kotlin.jetbrains.org) is an Apache 2 OSS Language targetted at the JVM and JavaScript and is developed by [JetBrains](http://www.jetbrains.com)
It is aimed at being a concise modern language for general use. It also rocks!

**Q: Is Kotlin free to use?**
<br/>While this is not a Kotlin FAQ, it is important to note that Kotlin is free to use and you can use the command line or the Community Edition
of IntelliJ to develop with it (which is free and OSS). Obviously IntelliJ Ultimate also works!
There's also an Eclipse plugin in the works. Check the project site for updates.


**Q: Is Spek a BDD or a TDD framework?**
<br/>Spek in Dutch means Bacon, so you could think of it as a Bacon Driven Development framework. Being serious for a
moment though, we (at least the original author) believe that there is a false distinction in frameworks around what TDD
or BDD is. Unit tests are ultimately about defining the specifications of your system. As such, Spek is merely a specification
framework if it can be called anything. For more information read [What BDD has taught me](http://hadihariri.com/2012/04/11/what-bdd-has-taught-me/)

**Q: Can I have more than one _on_ per _given_?**
<br/>Yes you can. How you group your specifications is up to you

**Q: Can I have more than one _it_ per _on_?**
<br/>Yes you can. In real world applications, an action can lead to several reactions.

**Q: Isn't it bad to have more than one assertion per test?**
<br/>Traditionally, in unit testing it's been recommended that you should limit each test to one assertion with the
idea that you test a single *unit*, and at the same time, find it easy to see where a test has failed. In Spek you can still
comply with this guidance. You can have multiple *it* but you limit each to one assertion.

**Q: Why is the logo a piece of bacon?**
<br/>We don't have a logo, but if you want to make one, let it be Bacon. English bacon that is. Spek was originally codenamed kspec. Someone suggested to rename it to Spek which kind of fits. Turns out, Spek is Bacon in
Dutch. So why not have bacon as a logo? After all, we love bacon!

**Q: Can I contribute?**
<br/>[Please do](http://github.com/jetbrains/spek)!


