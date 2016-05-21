package org.jetbrains.spek.console


fun main(args: Array<String>) {
    if (args.size < 2) {
        printUsage()
    } else {
        try {
            val options = getOptions(args)
            val specRunner = setupRunner(options)
            specRunner.runSpecs(options.paths, options.packageName)
        } catch (e: UnsupportedOperationException) {
            println("ERROR: ${e.message}")
            System.exit(1)
        } catch (e: Throwable) {
            println("ERROR: ${e.message}\n")
            e.printStackTrace()
            System.exit(1)
        }
    }
    System.exit(0)
}

fun getOptions(args: Array<String>): Options {
    var index = 2
    var format = "text"
    var filename = ""
    var paths = listOf<String>()
    var packageName = ""
    var verbose = false

    if (args.size > 0 ) {
        paths = args[0].split(',').toList()
    }
    if (args.size > 1) {
        packageName = args[1]
    }

    while (index < args.size) {
        when (args[index]) {
            "-f", "--format" -> {
                format = args[++index]
            }
            "-o", "--output" -> {
                filename = args[++index]
            }
            "-v", "--verbose" -> {
                verbose = true
            }
            else -> {
                throw UnsupportedOperationException("Unknown parameter: ${args[index]}")
            }
        }
        index++
    }

    if (format == "html" && filename == "") {
        filename = "out.html"
    }

    return Options(paths, packageName, format, filename, verbose)
}

fun setupRunner(options: Options): ConsoleSpekRunner {
    val notifier = CompositeNotifier()
    val device = if (options.filename != "") {
        FileOutputDevice(options.filename)
    } else {
        ConsoleOutputDevice()
    }

    when (options.format) {
        "text" -> {
            val deviceNotifier = if(options.verbose) OutputDeviceVerboseNotifier(device) else OutputDeviceNotifier(device)
            notifier.add(deviceNotifier)
        }
        "html" -> notifier.add(HtmlNotifier(options.packageName, device))
        else -> throw UnsupportedOperationException("Unknown format: ${options.format}")
    }
    return ConsoleSpekRunner(notifier)
}