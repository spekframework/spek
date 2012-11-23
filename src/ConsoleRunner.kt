package spek


import java.util.ArrayList

fun main(args: Array<String>) {


    val textOutput = PlainTextListener(ConsoleDevice())
    val listeners = ArrayList<Listener>()
    val multipleNotifiers = MultipleListenerNotifier(listeners)

    listeners.add(textOutput)
    val specRunner = SpecificationRunner(multipleNotifiers)

    specRunner.runSpecsInFolder(args[1])





}