package org.jetbrains.spek.console

fun printUsage() {
    println(
            """

    usage: spek <paths> <packageName> [options]
        where:
            <paths> comma separated files containing tests. Can be *.class or *.jar
            <packageName> package where tests are contained

        NOTE:  Assumes to be started with classpath that contains all spek classes and dependencies

        [options]:
            -f, --format <format>: Output format, default is text
                text: Output in plain text
                html: Output in HTML to file out.html
            -o, --output: Filename for main.output. Defaults to console

        example: spek path/to/tests org.spek --format text

    """)
}