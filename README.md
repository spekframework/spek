# Spek

A Specification framework for Kotlin

## License

Licensed under Modified BSD (See LICENSE.TXT) for full license

## Usage


Spek is a specification framework for Kotlin, allowing you to define your specifications
in a fluent and easy to read way.

### Writing specifications

A specification is a function (declared as fun in Kotlin) that has to be prefixed with the
annotation *spec* :


```kotlin
spec public fun calculatorSpecs() {
    given("a calculator")
    {
        val calculator = Calculator()

        on("calling sum with two numbers")
        {
            val sum = calculator.sum(2, 4)

            it("should return the result of adding the first number to the second number")
            {
                shouldEqual(6, sum)
            }
        }
    }
}
```

Since they are merely functions, specifications do not need to belong to a class and can be declared as top-level
functions in Kotlin. This gives you complete liberty to organize your specifications as you wish.

### Runners

Currently Spek comes with a Console Runner but a plugin for IntelliJ unit runner is planned.

## FAQ

**Q: What is Kotlin?**
<br/>[Kotlin](http://kotlin.jetbrains.org) is an Apache 2 OSS Language targetted at the JVM and JavaScript and is developed by [JetBrains](http://www.jetbrains.com)
It is aimed at being a concise modern language for general use. It also rocks!

**Q: Is Kotlin free to use?**
<br/>While this is not a Kotlin FAQ, it is important to note that Kotlin is free to use and you can use the command line or the Community Edition
of IntelliJ to develop with it (which is free and OSS). Obviously IntelliJ Ultimate also works!
There's also an Eclipse plugin in the works. Check the project site for updates.


**Q: Is Kotlin a BDD or a TDD framework?**
<br/>Spek in Belgian means Bacon, so you could think of it as a Bacon Driven Development framework. Being serious for a
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
Belgian. So why not have bacon as a logo? After all, we love bacon!

**Q: Can I contribute?**
<br/>Please do!

