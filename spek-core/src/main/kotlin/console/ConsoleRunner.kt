package org.spek.console

public fun main(vararg args: String) {
    if (args.size < 2) {
        printUsage()
    } else {
        val options = getOptions(args)
        val specRunner = setupRunner(options)
        specRunner.runSpecs(options.packageName)
    }
    //TODO: call System.exit to pass right exit code
}

fun getOptions(args: Array<String>): Options {
    var textPresent = false
    var htmlPresent = false
    var filename = ""
    var cssFile = ""
    var packageName = ""
    if (args.size >= 1) {
        packageName = args[0]
        textPresent = args.find { it.contains("-text") } != null
        htmlPresent = args.find { it.contains("-html") } != null
        val filePos = args.toList().indexOf("-file")
        if (filePos > 0) {
            filename = args[filePos + 1]
        }
        val cssPos = args.toList().indexOf("-css")
        if (cssPos > 0) {
            cssFile = args[cssPos + 1]
        }
    }
    return Options(packageName, textPresent, htmlPresent, filename, cssFile)
}

fun setupRunner(options: Options): Runner {
    val listeners = CompositeWorkflowReporter()

    var device: OutputDevice
    if (options.filename != "") {
        device = FileOutputDevice(options.filename)
    } else {
        device = ConsoleOutputDevice()
    }
    if (options.toText) {
        listeners.addListener(OutputDeviceWorkflowReporter(device))
    }
    if (options.toHtml) {
        throw RuntimeException("NOT supported")
        //        main.listeners.add(HTMLListener(device, options.cssFile))
    }
    return Runner(listeners)
}
