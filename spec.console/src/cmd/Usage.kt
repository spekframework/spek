package org.spek.console.cmd

fun printUsage() {
    println(
    """

    usage: spek path package -options

        options:

            -text: Output format in plain text
            -html: Output format in HTML
            -file: Filename for output. Defaults to console
            -css: Filename for CSS for HTML output


        example: spek path -text -html -file report.html
    """)
}
