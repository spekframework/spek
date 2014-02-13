package org.spek.console

public fun main(vararg args: String) {
    if (args.size == 0) {
        printUsage()
    } else {
        try {
            val options = getOptions(args)
            val specRunner = setupRunner(options)
            specRunner.runSpecs(options.packageName)
        } catch (e: UnsupportedOperationException) {
            println("ERROR: ${e.getMessage()}")
            System.exit(2)
        } catch (e: Throwable) {
            println("ERROR: ${e.getMessage()}\n")
            e.printStackTrace()
            System.exit(2)
        }
    }
    System.exit(0)
}

fun getOptions(args: Array<String>): Options {
    var index = 1
    var format = "text"
    var filename = ""
    var cssFile = ""
    var packageName = args[0]

    while (index < args.size) {
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

    return Options(packageName, format, filename, cssFile)
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
