package org.jetbrains.spek.console


public fun main(args: Array<String>)  {
    if (args.size() < 2) {
        printUsage()
    } else {
        try {
            val options = getOptions(args)
            val specRunner = setupRunner(options)
            specRunner.runSpecs(options.paths, options.packageName)
        } catch (e: UnsupportedOperationException) {
            println("ERROR: ${e.getMessage()}")
            System.exit(1)
        } catch (e: Throwable) {
            println("ERROR: ${e.getMessage()}\n")
            e.printStackTrace()
            System.exit(1)
        }
    }
    System.exit(0)
}

public fun getOptions(args: Array<String>): Options {
    var index = 2
    var format = "text"
    var filename = ""
    var cssFile = ""
    var paths = listOf<String>()
    var packageName = ""

    if (args.size() > 0 ) {
        paths = args[0].split(',').toList()
    }
    if (args.size() > 1) {
        packageName = args[1]
    }

    while (index < args.size()) {
        when (args[index]) {
            "-f", "--format" -> {
                format = args[++index]
            }
            "-o", "--output" -> {
                filename = args[++index]
            }
            "-s", "--css" -> {
                cssFile = args[++index]
            }
            else -> {
                throw UnsupportedOperationException("Unknown parameter: ${args[index]}")
            }
        }
        index++
    }

    return Options(paths, packageName, format, filename, cssFile)
}

fun setupRunner(options: Options): Runner {
    val listeners = CompositeWorkflowReporter()
    val device = if (options.filename != "") {
        FileOutputDevice(options.filename)
    } else {
        ConsoleOutputDevice()
    }

    when (options.format) {
        "text" -> listeners.addListener(OutputDeviceWorkflowReporter(device))
        "html" -> listeners.addListener(HtmlWorkflowReporter(options.packageName, device, options.cssFile))
        else -> throw UnsupportedOperationException("Unknown format: ${options.format}")
    }
    return Runner(listeners)
}
