package org.spek.test

import kotlin.test.assertNotNull
import org.junit.Test as test
import java.util.ArrayList
import kotlin.test.assertEquals
import org.spek.PlainTextListener
import org.spek.Listener
import org.spek.MultipleListenerNotifier
import org.spek.SpecificationRunner

public class TestConsoleRunner {

    test fun tempTest() {

        val textOutput = PlainTextListener(ConsoleDevice())
        val htmlOutput = HTMLListener(ConsoleDevice(), "")
        val listeners = ArrayList<Listener>()
        val multipleNotifiers = MultipleListenerNotifier(listeners)

        listeners.add(textOutput)
        listeners.add(htmlOutput)
        val specRunner = SpecificationRunner(multipleNotifiers)
        try {
            specRunner.runSpecsInFolder("org.jetbrains.spek.test.samples")
        } finally {
            specRunner.close()
        }


    }



}