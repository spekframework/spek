---
layout: default
title: Spek - A Specification Framework
---



<br/>

### Your typical test code
Here's some typical test code found in many codebases
{% highlight java %}
@Test
public void testCalculateTaxRate() {

    TaxRateCalculator calculator = new TaxRateCalculator();

    Int value = calculator.calculateRate(200, 10);

    assertEquals(300,value);
}
{% endhighlight %}


This code suffers from several issues. Under what **conditions** is the tax rate calculated? What exactly is it **doing**? What is the **expected outcome**?


### Being concise with Spek


Spek makes it easy to define these three important aspects without restoring to long method names or underscores:

{% highlight kotlin %}
class TaxCalculatorSpecs: Spek() {{ "{{" }}

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

### Tests are specifications

Tests are executable specifications of your system. They are real-time compilable code that tell you how a piece of code should behave and under what conditions is a certain
outcome expected. That is why it is important to be explicit, concise and unambiguous when it comes to defining specifications. Spek helps you do that.

### Spek works with Java

Spek is written in Kotlin and as such is 100% compatible with Java. You can write your specifications (notice we say specification, not test) in Kotlin and
test new or existing code written in Java or Kotlin.

<br/>
<a href="{{ site.url }}/download.html" class="btn btn-success btn-lg">Get Started</a>
<a href="{{ site.url }}/docs.html" class="btn btn-primary btn-lg">Learn More</a>
