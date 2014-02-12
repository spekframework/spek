package org.spek.console

fun printUsage() {
    println(
            """

    usage: spek <package> <options>
        where:
            <package> base package to search for speks to run tests

        NOTE.  Same as JUnit Spek runner assumes to be started
               with classpath that contains all spek classes
               and dependencies

        NOTE2. You may use JUnitSpek base class to turn all
               speks into JUnit tests on run-time

        options:
            -text: Output format in plain text
            -html: Output format in HTML
            -file: Filename for main.output. Defaults to console
            -css: Filename for CSS for HTML main.output

        example: spek org.spek -text

    """)
}
