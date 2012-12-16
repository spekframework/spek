package org.spek

import java.util.ArrayList

fun main(args: Array<String>) {

    if (args.size < 2) {
        printUsage()
    } else {
        val options = getOptions(args)
        val specRunner = setupRunner(options)
        specRunner.runSpecs(options.path, options.packageName)
    }
}

fun printUsage() {

    println("usage: spek folder package -options")
    println("")
    println("  options:")
    println("")
    println("       -text: Output format in plain text")
    println("       -html: Output format in HTML")
    println("       -file: Filename for output. Defaults to console")
    println("       -css: Filename for CSS for HTML output")
    println("")
    println("example: spek myapp -text -html -file report.html")
}

fun getOptions(args: Array<String>): Options {

    var path = ""
    var textPresent = false
    var htmlPresent = false
    var filename = ""
    var cssFile = ""
    var packageName = ""
    if (args.size >= 2) {
        path = args[0]
        packageName = args[1]
        textPresent = args.find { it.contains("-text")} != null
        htmlPresent = args.find { it.contains("-html")} != null
        val filePos = args.toList().indexOf("-file")
        if (filePos > 0) {
            filename = args[filePos+1]
        }
        val cssPos = args.toList().indexOf("-css")
        if (cssPos > 0) {
            cssFile = args[cssPos+1]
        }
    }
    return Options(path, packageName, textPresent, htmlPresent, filename, cssFile)
}

fun setupRunner(options: Options): SpecificationRunner {

    val listeners = ArrayList<Listener>()
    val multipleNotifiers = MultipleListenerNotifier(listeners)

    var device: OutputDevice
    if (options.filename != "") {
        device = FileDevice(options.filename)
    } else {
        device = ConsoleDevice()
    }
    if (options.toText) {
        listeners.add(PlainTextListener(device))
    }
    if (options.toHtml) {
        listeners.add(HTMLListener(device, options.cssFile))
    }
    return SpecificationRunner(multipleNotifiers)
}

data class Options(val path: String, val packageName: String, val toText: Boolean, val toHtml: Boolean, val filename: String, val cssFile: String)