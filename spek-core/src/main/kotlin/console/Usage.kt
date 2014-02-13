package org.spek.console

fun printUsage() {
    println(
            """

    usage: spek <package> [options]
        where:
            <package> base package to search for speks to run tests

        NOTE:  Assumes to be started with classpath that contains all spek classes and dependencies

        [options]:
            -format <format>: Output format, default is text
                text: Output in plain text
                html: Output in HTML
            -file: Filename for main.output. Defaults to console
            -css: Filename for CSS for HTML main.output

        example: spek org.spek -text

    """)
}
