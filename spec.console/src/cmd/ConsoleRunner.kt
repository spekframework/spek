package org.spek.console.cmd

import org.spek.impl.events.*
import org.spek.console.reflect.*
import org.spek.console.listeners.*
import org.spek.console.listeners.text.*
import org.spek.console.output.file.*
import org.spek.console.output.console.*

fun main(args: Array<String>) {
    if (args.size < 2) {
        printUsage()
    } else {
        val options = getOptions(args)
        val specRunner = setupRunner(options)
        specRunner.runSpecs(options.packageName)
    }
}

fun getOptions(args: Array<String>): Options {
    var textPresent = false
    var htmlPresent = false
    var filename = ""
    var cssFile = ""
    var packageName = ""
    if (args.size >= 1) {
        packageName = args[0]
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
    return Options(packageName, textPresent, htmlPresent, filename, cssFile)
}

fun setupRunner(options: Options): SpecificationRunner {
    val listeners = Multicaster()

    var device: OutputDevice
    if (options.filename != "") {
        device = FileDevice(options.filename)
    } else {
        device = ConsoleDevice()
    }
    if (options.toText) {
        listeners.addListener(PlainTextListener(device))
    }
    if (options.toHtml) {
        throw RuntimeException("NOT supported")
//        listeners.add(HTMLListener(device, options.cssFile))
    }
    return SpecificationRunner(listeners)
}
